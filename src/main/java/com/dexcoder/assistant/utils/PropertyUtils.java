package com.dexcoder.assistant.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 属性文件操作辅助类
 * 
 * User: liyd
 * Date: 14-1-7
 * Time: 上午11:24
 */
public final class PropertyUtils {

    private static final Logger        LOG                = LoggerFactory
                                                              .getLogger(PropertyUtils.class);

    /** 属性文件后缀 */
    private static final String        PRO_SUFFIX         = ".properties";

    /** 默认的web容器 */
    private static final String        DEFAULT_WEB_SERVER = "tomcat";

    /** 配置文件保存map */
    private static Map<String, String> propMap            = new HashMap<String, String>();

    /**
     * 加载properties文件
     *
     * @param webServer the web server
     * @param resourceName the resource name
     */
    public static void loadProperties(String webServer, String resourceName) {

        try {
            if (!StringUtils.endsWith(resourceName, PRO_SUFFIX)) {
                resourceName += PRO_SUFFIX;
            }
            Properties prop = new Properties();
            String confPath = getWebServerConfPath(webServer);
            File file = new File(confPath, resourceName);
            if (StringUtils.isBlank(confPath) || !file.exists()) {
                LOG.info("从classpath加载配置文件{}", resourceName);
                InputStream stream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(resourceName);
                prop.load(stream);
            } else {
                LOG.info("从web容器配置目录加载配置文件{}", resourceName);
                prop.load(new FileReader(file));
            }
            Iterator<Map.Entry<Object, Object>> iterator = prop.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                propMap.put(resourceName + String.valueOf(entry.getKey()),
                    String.valueOf(entry.getValue()));
            }
            //为配置文件加入一个属性，用以判断该配置文件已加载过
            propMap.put(resourceName, "true");
        } catch (IOException e) {
            //ignore
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
     * @param key the key
     * @param defaultValue 不存在时返回的默认值
     * @return property
     */
    public static String getProperty(String resourceName, String key, String defaultValue) {
        if (!StringUtils.endsWith(resourceName, PRO_SUFFIX)) {
            resourceName += PRO_SUFFIX;
        }
        String finalKey = resourceName + key;
        if (propMap.get(resourceName) == null) {
            loadProperties(DEFAULT_WEB_SERVER, resourceName);
        }
        String value = propMap.get(finalKey);
        return StringUtils.isBlank(value) ? defaultValue : value;
    }

    /**
     * 获取web容器的配置目录
     *  
     * @param webServer
     * @return
     */
    private static String getWebServerConfPath(String webServer) {

        if (StringUtils.equalsIgnoreCase(webServer, "tomcat")) {
            return System.getProperty("catalina.home") + "/conf";
        }
        return null;
    }
}
