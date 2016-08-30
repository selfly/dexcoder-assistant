package com.dexcoder.dal.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import com.dexcoder.commons.bean.BeanConverter;
import com.dexcoder.commons.bean.LongIntegerConverter;
import com.dexcoder.commons.pager.Pager;
import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.dal.SqlFactory;
import com.dexcoder.dal.handler.DefaultMappingHandler;
import com.dexcoder.dal.handler.KeyGenerator;
import com.dexcoder.dal.handler.MappingHandler;
import com.dexcoder.dal.spring.mapper.JdbcRowMapper;
import com.dexcoder.dal.spring.page.PageControl;

/**
 * Created by liyd on 2015-12-15.
 */
public abstract class AbstractJdbcDaoImpl {

    protected static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * spring jdbcTemplate 对象
     */
    protected JdbcOperations        jdbcTemplate;

    /**
     * 名称处理器，为空按默认执行
     */
    protected MappingHandler        mappingHandler;

    /** 主键生成器 为空默认数据库自增 */
    protected KeyGenerator          keyGenerator;

    /**
     * rowMapper，为空按默认执行
     */
    protected String                rowMapperClass;

    /**
     * 自定义sql处理
     */
    protected SqlFactory            sqlFactory;

    /**
     * 数据库方言
     */
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
        BeanConverter.registerConverter(new LongIntegerConverter(Long.class, Integer.class));
        return BeanConverter.underlineKeyMapToBean(map, beanClass);
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
        BeanConverter.registerConverter(new LongIntegerConverter(Long.class, Integer.class));
        List<T> beans = BeanConverter.underlineKeyMapToBean(mapList, beanClass);
        Pager pager = PageControl.getPager();
        if (pager != null) {
            pager.setList(beans);
            PageControl.setPager(pager);
        }
        return beans;
    }

    /**
     * 获取rowMapper对象
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> RowMapper<T> getRowMapper(Class<T> clazz) {

        if (StringUtils.isBlank(rowMapperClass)) {
            return JdbcRowMapper.newInstance(clazz);
        } else {
            return (RowMapper<T>) ClassUtils.newInstance(rowMapperClass);
        }
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

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    protected String getDialect() {
        if (StringUtils.isBlank(dialect)) {
            dialect = jdbcTemplate.execute(new ConnectionCallback<String>() {
                public String doInConnection(Connection con) throws SQLException, DataAccessException {
                    return con.getMetaData().getDatabaseProductName().toUpperCase();
                }
            });
        }
        return dialect;
    }

    public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public void setRowMapperClass(String rowMapperClass) {
        this.rowMapperClass = rowMapperClass;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public void setSqlFactory(SqlFactory sqlFactory) {
        this.sqlFactory = sqlFactory;
    }

}
