package com.dexcoder.assistant.persistence;

import com.dexcoder.assistant.exceptions.AssistantException;
import com.dexcoder.assistant.utils.ClassUtils;
import com.dexcoder.assistant.utils.PropertyUtils;
import com.dexcoder.assistant.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by liyd on 2015-10-30.
 */
public class DynamicDataSourceUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicDataSourceUtils.class);

    /**
     * 设置数据源权重量
     *
     * @param readDsList  the read ds list
     * @param writeDsList the write ds list
     * @param dsId        the ds id
     * @param weight      the weight
     */
    public static void addWeightDataSource(List<String> readDsList, List<String> writeDsList,
                                           String dsId, int weight, String mode) {

        if (StrUtils.equalsIgnoreCase(mode, DynamicDataSource.DS_MODE_R)) {
            for (int i = 0; i < weight; i++) {
                readDsList.add(dsId);
            }
        } else if (StrUtils.equalsIgnoreCase(mode, DynamicDataSource.DS_MODE_W)) {
            for (int i = 0; i < weight; i++) {
                writeDsList.add(dsId);
            }
        } else if (StrUtils.equalsIgnoreCase(mode, DynamicDataSource.DS_MODE_RW)) {
            for (int i = 0; i < weight; i++) {
                readDsList.add(dsId);
                writeDsList.add(dsId);
            }
        } else {
            throw new AssistantException("动态数据源读写模式设置错误,应为其中一种:[mode=r,mode=w,mode=rw]");
        }
    }

    /**
     * 设置数据源各项属性
     *
     * @param map
     * @param dataSource
     */
    public static void setDsProperties(Map<String, String> map, DataSource dataSource) {

        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {

            String name = entry.getKey();
            PropertyDescriptor propertyDescriptor = ClassUtils.getPropertyDescriptor(
                    dataSource.getClass(), name);
            if (propertyDescriptor == null || propertyDescriptor.getWriteMethod() == null) {
                LOG.warn("设置的数据源属性{}不存在", name);
                continue;
            }

            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                writeMethod.setAccessible(true);
            }
            ClassUtils.invokeMethod(writeMethod, dataSource, entry.getValue());
        }
    }

    /**
     * 获取并移除元素
     *
     * @param map
     * @return
     */
    public static String getAndRemoveValue(Map<String, String> map, String key, String defaultValue) {
        String value = map.get(key);
        map.remove(key);
        if (StrUtils.isBlank(value) && StrUtils.isBlank(defaultValue)) {
            LOG.error("属性不能为空:{}", key);
            throw new AssistantException("属性不能为空:" + key);
        }
        return StrUtils.isBlank(value) ? defaultValue : value;
    }

    /**
     * 解析数据源信息
     *
     * @param dsConfigFile
     * @return
     */
    public static List<Map<String, String>> parseDataSources(String dsConfigFile) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = documentBuilder.parse(PropertyUtils.loadResource(dsConfigFile));
            Element dataSources = doc.getDocumentElement();
            NodeList datasourceNodes = dataSources.getChildNodes();
            if (datasourceNodes == null || datasourceNodes.getLength() == 0) {
                LOG.error("动态数据源配置信息错误");
                throw new AssistantException("动态数据源配置信息错误");
            }

            List<Map<String, String>> dataSourceList = new ArrayList<Map<String, String>>(10);
            for (int i = 0; i < datasourceNodes.getLength(); i++) {
                Node node = datasourceNodes.item(i);
                if (node.getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                String dataSourceId = getAttr(node, DynamicDataSource.ATTR_ID);
                String dataSourceClass = getAttr(node, DynamicDataSource.ATTR_CLASS);
                String isDefaultDataSource = getAttr(node, DynamicDataSource.ATTR_DEFAULT);

                NodeList propertyNodeList = node.getChildNodes();
                Map<String, String> proMap = parseProperties(propertyNodeList);
                proMap.put(DynamicDataSource.ATTR_ID, dataSourceId);
                proMap.put(DynamicDataSource.ATTR_CLASS, dataSourceClass);
                proMap.put(DynamicDataSource.ATTR_DEFAULT, isDefaultDataSource);
                dataSourceList.add(proMap);
            }
            return dataSourceList;
        } catch (Exception e) {
            LOG.error("解析动态数据源配置文件出错:" + dsConfigFile, e);
            throw new AssistantException(e);
        }
    }

    /**
     * 获取属性值
     *
     * @param node
     * @param attrName
     * @return
     */
    private static String getAttr(Node node, String attrName) {
        NamedNodeMap attributes = node.getAttributes();
        Node namedItem = attributes.getNamedItem(attrName);
        if (namedItem == null) {
            return null;
        }
        return namedItem.getNodeValue();
    }

    /**
     * 解析property标签
     *
     * @param propertyNodeList
     * @return
     */
    private static Map<String, String> parseProperties(NodeList propertyNodeList) {

        Map<String, String> propertyMap = new HashMap<String, String>();
        if (propertyNodeList == null || propertyNodeList.getLength() == 0) {
            return propertyMap;
        }
        for (int i = 0; i < propertyNodeList.getLength(); i++) {
            Node node = propertyNodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            NamedNodeMap attributes = node.getAttributes();
            String name = attributes.getNamedItem(DynamicDataSource.ATTR_NAME).getNodeValue();
            String value = attributes.getNamedItem(DynamicDataSource.ATTR_VALUE).getNodeValue();
            propertyMap.put(name, value);
        }
        return propertyMap;
    }
}
