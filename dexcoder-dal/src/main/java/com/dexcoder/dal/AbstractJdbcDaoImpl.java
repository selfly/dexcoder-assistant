package com.dexcoder.dal;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.dexcoder.commons.bean.BeanKit;
import com.dexcoder.commons.bean.LongIntegerConverter;
import com.dexcoder.commons.page.PageList;
import com.dexcoder.dal.handler.DefaultMappingHandler;
import com.dexcoder.dal.handler.KeyGenerator;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * Created by liyd on 2015-12-15.
 */
public abstract class AbstractJdbcDaoImpl {

    protected static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /** 名称处理器，为空按默认执行 */
    protected MappingHandler        mappingHandler;

    /** 主键生成器 为空默认数据库自增 */
    protected KeyGenerator          keyGenerator;

    /** 自定义sql处理 */
    protected SqlFactory            sqlFactory;

    /** 数据库方言 */
    protected String                dialect;

    /**
     * map转bean
     * 
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     */
    protected <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        BeanKit.registerConverter(new LongIntegerConverter(Long.class, Integer.class));
        return BeanKit.underlineKeyMapToBean(map, beanClass);
    }

    /**
     * map转bean
     *
     * @param <T>  the type parameter
     * @param mapList the map list
     * @param beanClass the bean class
     * @return list
     */
    protected <T> List<T> mapToBean(List<Map<String, Object>> mapList, Class<T> beanClass) {
        if (CollectionUtils.isEmpty(mapList)) {
            return null;
        }
        BeanKit.registerConverter(new LongIntegerConverter(Long.class, Integer.class));
        List<T> beans = BeanKit.underlineKeyMapToBean(mapList, beanClass);
        //如果是分页的
        if (mapList instanceof PageList) {
            return new PageList<T>(beans, ((PageList<?>) mapList).getPager());
        }
        return beans;
    }

    /**
     * 获取名称处理器
     *
     * @return
     */
    protected MappingHandler getMappingHandler() {

        if (this.mappingHandler == null) {
            this.mappingHandler = new DefaultMappingHandler();
        }
        return this.mappingHandler;
    }

    /**
     * 获取数据库方言,由具体子类实现
     * 
     * @return
     */
    public abstract String getDialect();

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public void setSqlFactory(SqlFactory sqlFactory) {
        this.sqlFactory = sqlFactory;
    }

}
