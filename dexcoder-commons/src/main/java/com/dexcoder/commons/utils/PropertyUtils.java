package com.dexcoder.commons.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

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
    private static final String                           PRO_SUFFIX   = ".properties";

    /**
     * 配置文件保存map
     */
    private final static Map<String, Map<String, String>> RESOURCE_MAP = new HashMap<String, Map<String, String>>();

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
            throw new CommonsAssistantException("加载文件失败:" + resourceName, e);
        }
    }

    /**
     * 获取properties文件所有属性
     * 
     * @param resourceName
     * @return
     */
    public static Map<String, String> getProperties(String resourceName) {

        String propertyFileName = getPropertyFileName(resourceName);

        try {

            Map<String, String> propMap = RESOURCE_MAP.get(propertyFileName);
            if (propMap == null) {

                propMap = new HashMap<String, String>();
                Properties prop = new Properties();
                prop.load(loadResource(propertyFileName));
                Iterator<Map.Entry<Object, Object>> iterator = prop.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<Object, Object> entry = iterator.next();
                    propMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
                //加入配置文件属性
                RESOURCE_MAP.put(propertyFileName, propMap);
            }

            return propMap;
        } catch (IOException e) {
            throw new CommonsAssistantException("加载配置文件失败:" + propertyFileName, e);
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
        String propertyFileName = getPropertyFileName(resourceName);
        Map<String, String> map = getProperties(propertyFileName);
        String value = map.get(key);
        return StringUtils.isBlank(value) ? defaultValue : value;
    }

    /**
     * 获取properties文件后缀的名称
     * 
     * @param resourceName
     * @return
     */
    private static String getPropertyFileName(String resourceName) {

        String propertyFileName = resourceName;
        if (!StringUtils.endsWith(resourceName, PRO_SUFFIX)) {
            propertyFileName += PRO_SUFFIX;
        }
        return propertyFileName;
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
