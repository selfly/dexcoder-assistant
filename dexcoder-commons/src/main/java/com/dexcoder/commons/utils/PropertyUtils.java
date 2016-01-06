package com.dexcoder.commons.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * 属性文件操作辅助类
 * <p/>
 * User: liyd
 * Date: 14-1-7
 * Time: 上午11:24
 */
public final class PropertyUtils {

    /**
     * 属性文件后缀
     */
    private static final String        PRO_SUFFIX = ".properties";

    /**
     * 配置文件保存map
     */
    private static Map<String, String> propMap    = new HashMap<String, String>();

    /**
     * 加载资源文件
     *
     * @param resourceName
     * @return
     */
    public static InputStream loadResource(String resourceName) {

        try {
            File configFile = getConfigFile(resourceName);
            if (configFile == null) {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
                return is;
            } else {
                return new FileInputStream(configFile);
            }
        } catch (FileNotFoundException e) {
            throw new CommonsAssistantException("加载xml文件失败:" + resourceName, e);
        }
    }

    /**
     * 加载properties文件
     *
     * @param resourceName the resource name
     */
    public static void loadProperties(String resourceName) {

        try {
            if (!StrUtils.endsWith(resourceName, PRO_SUFFIX)) {
                resourceName += PRO_SUFFIX;
            }
            Properties prop = new Properties();
            prop.load(loadResource(resourceName));
            Iterator<Map.Entry<Object, Object>> iterator = prop.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                propMap.put(resourceName + String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
            //为配置文件加入一个属性，用以判断该配置文件已加载过
            propMap.put(resourceName, "true");
        } catch (IOException e) {
            throw new CommonsAssistantException("加载配置文件失败:" + resourceName, e);
        }
    }

    /**
     * 根据key获取properties文件的value值
     *
     * @param resourceName properties文件名
     * @param key
     * @return
     */
    public static String getProperty(String resourceName, String key) {
        return getProperty(resourceName, key, null);
    }

    /**
     * 根据key获取properties文件的value值
     *
     * @param resourceName properties文件名
     * @param key          the key
     * @param defaultValue 不存在时返回的默认值
     * @return property
     */
    public static String getProperty(String resourceName, String key, String defaultValue) {
        if (!StrUtils.endsWith(resourceName, PRO_SUFFIX)) {
            resourceName += PRO_SUFFIX;
        }
        String finalKey = resourceName + key;
        if (propMap.get(resourceName) == null) {
            loadProperties(resourceName);
        }
        String value = propMap.get(finalKey);
        return StrUtils.isBlank(value) ? defaultValue : value;
    }

    /**
     * 获取web容器的配置目录
     *
     * @return
     */
    private static File getConfigFile(String resourceName) {

        //tomcat
        String resourcePath = System.getProperty("catalina.home") + "/conf";
        File file = new File(resourcePath, resourceName);
        if (file.exists()) {
            return file;
        }
        //程序目录
        resourcePath = System.getProperty("user.dir");
        file = new File(resourcePath, resourceName);
        if (file.exists()) {
            return file;
        }
        return null;
    }
}
