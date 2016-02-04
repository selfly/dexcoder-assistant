package com.dexcoder.dal.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.dexcoder.commons.bean.BeanConverter;
import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.commons.utils.NameUtils;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.SqlFactory;
import com.dexcoder.dal.handler.DefaultMappingHandler;
import com.dexcoder.dal.handler.MappingHandler;

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
     * 插入数据
     *
     * @param boundSql the bound build
     * @return long long
     */
    protected Long insert(final BoundSql boundSql, final Class<?> clazz) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String pkColumnName = getMappingHandler().getPkColumnName(clazz);
                PreparedStatement ps = con.prepareStatement(boundSql.getSql(), new String[] { pkColumnName });
                int index = 0;
                for (Object param : boundSql.getParameters()) {
                    index++;
                    ps.setObject(index, param);
                }
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * map转bean
     * 
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     */
    protected <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        return BeanConverter.mapToBean(map, beanClass);
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
        return BeanConverter.mapToBean(mapList, beanClass);
    }

    /**
     * 转换map中的key column为field
     *
     * @param map the map
     * @return map
     */
    protected Map<String, Object> convertMapKeyToCamel(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String camelName = StrUtils.indexOf(entry.getKey(), "_") != -1 ? NameUtils.getCamelName(entry.getKey())
                : entry.getKey().toLowerCase();
            resultMap.put(camelName, entry.getValue());
        }
        return resultMap;
    }

    /**
     * 转换map中的key column为field
     *
     * @param mapList
     * @return
     */
    protected List<Map<String, Object>> convertMapKeyToCamel(List<Map<String, Object>> mapList) {
        if (mapList == null) {
            return null;
        }
        List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>(mapList.size());
        for (Map<String, Object> map : mapList) {
            Map<String, Object> resultMap = convertMapKeyToCamel(map);
            resultMapList.add(resultMap);
        }
        return resultMapList;
    }

    /**
     * 获取rowMapper对象
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> RowMapper<T> getRowMapper(Class<T> clazz) {

        if (StrUtils.isBlank(rowMapperClass)) {
            return BeanPropertyRowMapper.newInstance(clazz);
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

    protected String getDialect() {
        if (StrUtils.isBlank(dialect)) {
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
