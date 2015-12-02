//package com.dexcoder.assistant.persistence;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.collections.MapUtils;
//import org.apache.commons.lang.StringUtils;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//
//import com.dexcoder.assistant.exceptions.AssistantException;
//import com.dexcoder.assistant.utils.CacheUtils;
//import com.dexcoder.assistant.utils.VelocityUtils;
//
///**
// * 自定义sql文件辅助类
// *
// * Created by liyd on 2015-11-17.
// */
//public class SqlHandler {
//
//    private static final Logger LOG        = LoggerFactory.getLogger(SqlHandler.class);
//
//    private static final String ATTR_ID    = "id";
//    private static final String ATTR_REFID = "refid";
//
//    public SqlHandler(String sqlLocation) {
//        String[] sqlLocations = StringUtils.split(sqlLocation, ",");
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        for (String location : sqlLocations) {
//            try {
//                Resource[] resources = resourcePatternResolver.getResources(location);
//                this.readResource(resources);
//            } catch (Exception e) {
//                LOG.error("读取sqlLocation失败:" + location, e);
//                throw new AssistantException("读取sqlLocation失败:" + location);
//            }
//        }
//    }
//
//    public Map<String, Object> propToMap(String name, Object object) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put(name, object);
//        return map;
//    }
//
//    /**
//     * 获取sql
//     *
//     * @param sqlId
//     * @param map
//     * @return
//     */
//    public BoundSql getSql(String sqlId, Map<String, Object> map) {
//
//        String sql = CacheUtils.getForString(sqlId);
//        if (StringUtils.isBlank(sql)) {
//            throw new AssistantException("sql不存在:" + sqlId);
//        }
//        sql = VelocityUtils.render(sql, (Map) map);
//        List<Object> args = new ArrayList<Object>();
//        if (MapUtils.isNotEmpty(map)) {
//            ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
//            GenericTokenParser parser = new GenericTokenParser("#{", "}", tokenHandler);
//            sql = parser.parse(sql);
//            List<String> parameters = tokenHandler.getParameters();
//
//            for (String parameter : parameters) {
//                Object value = this.getValue(parameter, map);
//                args.add(value);
//            }
//        }
//        return new BoundSql(sql, null, args);
//    }
//
//    /**
//     *  获取sql
//     *
//     * @param sqlId
//     * @return
//     */
//    public BoundSql getSql(String sqlId) {
//        return this.getSql(sqlId, null);
//    }
//
//    /**
//     * 获取value
//     *
//     * @param parameter
//     * @param object
//     * @return
//     */
//    private Object getValue(String parameter, Object object) {
//
//        PropertyTokenizer prop = new PropertyTokenizer(parameter, object);
//        Object value = prop.getObjectWrapper().getValue(prop.getName());
//        if (prop.hasNext()) {
//            return getValue(prop.getChildren(), value);
//            //            if (value == null) {
//            //                return null;
//            //            } else {
//            //                return metaValue.getValue(prop.getChildren());
//            //            }
//        } else {
//            return value;
//        }
//    }
//
//    /**
//     * 读取resource
//     *
//     * @param resources
//     */
//    private void readResource(Resource[] resources) {
//
//        for (Resource resource : resources) {
//
//            try {
//                SAXReader reader = new SAXReader();
//                Document document = reader.read(resource.getFile());
//                //解析变量
//                this.parseSql(document.selectNodes("/mapper/sql"));
//
//                //解析sql
//                this.parseSql(document.getRootElement().selectNodes("select|insert|update|delete"));
//            } catch (Exception e) {
//                LOG.error("读取resource文件失败:" + resource.getFilename(), e);
//                throw new AssistantException("读取resource文件失败:" + resource.getFilename());
//            }
//        }
//    }
//
//    /**
//     * 解析sql
//     *
//     * @param elements
//     */
//    private void parseSql(List<Element> elements) {
//
//        if (CollectionUtils.isEmpty(elements)) {
//            return;
//        }
//        for (Element element : elements) {
//            this.parseIncludeSql(element);
//            String sqlId = element.attributeValue(ATTR_ID);
//            String sql = StringUtils.trim(element.getStringValue());
//            Object obj = CacheUtils.get(sqlId);
//            if (obj != null) {
//                throw new AssistantException("sql id重复: " + sqlId);
//            }
//            //永不失效
//            CacheUtils.put(sqlId, sql, 0);
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("sqlId={},sql={}", sqlId, sql);
//            }
//        }
//    }
//
//    /**
//     * 解析include变量sql
//     *
//     * @param element
//     */
//    private void parseIncludeSql(Element element) {
//
//        List<Element> includeEl = element.elements("include");
//        if (CollectionUtils.isEmpty(includeEl)) {
//            return;
//        }
//        for (Element el : includeEl) {
//
//            //递归解析
//            parseIncludeSql(el);
//            String refId = el.attributeValue(ATTR_REFID);
//            String refSql = CacheUtils.getForString(refId);
//            if (StringUtils.isBlank(refSql)) {
//                throw new AssistantException("include的sql变量不存在:" + refId);
//            }
//            el.setText(refSql);
//        }
//    }
//}
