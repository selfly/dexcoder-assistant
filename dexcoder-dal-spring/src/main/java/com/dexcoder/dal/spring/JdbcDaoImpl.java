package com.dexcoder.dal.spring;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.JdbcDao;
import com.dexcoder.dal.build.AbstractSqlBuilder;
import com.dexcoder.dal.build.Criteria;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * jdbc操作dao
 * <p/>
 * Created by liyd on 3/3/15.
 */
@SuppressWarnings("unchecked")
public class JdbcDaoImpl extends AbstractJdbcDaoImpl implements JdbcDao {

    public Long insert(Object entity) {
        MappingHandler handler = this.getMappingHandler();
        Criteria criteria = Criteria.insert(entity.getClass());
        String nativePKValue = handler.getPkNativeValue(entity.getClass(), getDialect());
        if (StrUtils.isNotBlank(nativePKValue)) {
            String pkFieldName = handler.getPkFieldName(entity.getClass());
            criteria.into(AbstractSqlBuilder.NATIVE_TOKENS[2] + pkFieldName + AbstractSqlBuilder.NATIVE_TOKENS[3],
                nativePKValue);
        }
        final BoundSql boundSql = criteria.build(entity, true, getMappingHandler());
        return this.insert(boundSql, entity.getClass());
    }

    public Long insert(Criteria criteria) {
        final BoundSql boundSql = criteria.build(true, getMappingHandler());
        return this.insert(boundSql, criteria.getEntityClass());
    }

    public void save(Object entity) {
        final BoundSql boundSql = Criteria.insert(entity.getClass()).build(entity, true, getMappingHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public void save(Criteria criteria) {
        final BoundSql boundSql = criteria.build(true, getMappingHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getMappingHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Object entity) {
        BoundSql boundSql = Criteria.update(entity.getClass()).build(entity, true, getMappingHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Object entity, boolean isIgnoreNull) {
        BoundSql boundSql = Criteria.update(entity.getClass()).build(entity, isIgnoreNull, getMappingHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getMappingHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Object entity) {
        BoundSql boundSql = Criteria.delete(entity.getClass()).build(entity, true, getMappingHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Class<?> clazz, Long id) {
        Criteria criteria = Criteria.delete(clazz);
        BoundSql boundSql = criteria.where(criteria.getPkField(getMappingHandler()), new Object[] { id }).build(true,
            getMappingHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public <T> List<T> queryList(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getMappingHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        return (List<T>) list;
    }

    public <T> List<T> queryList(Class<?> clazz) {
        BoundSql boundSql = Criteria.select(clazz).build(true, getMappingHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(clazz));
        return (List<T>) list;
    }

    public <T> List<T> queryList(T entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).build(entity, true, getMappingHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    public <T> List<T> queryList(T entity, Criteria criteria) {
        BoundSql boundSql = criteria.build(entity, true, getMappingHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    public int queryCount(Class<?> clazz) {
        BoundSql boundSql = Criteria.select(clazz).addSelectFunc("count(*)").build(null, true, getMappingHandler());
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public int queryCount(Object entity, Criteria criteria) {
        BoundSql boundSql = criteria.addSelectFunc("count(*)").build(entity, true, getMappingHandler());
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public int queryCount(Object entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).addSelectFunc("count(*)")
            .build(entity, true, getMappingHandler());
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public int queryCount(Criteria criteria) {
        BoundSql boundSql = criteria.addSelectFunc("count(*)").build(true, getMappingHandler());
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public <T> T get(Class<T> clazz, Long id) {
        Criteria criteria = Criteria.select(clazz);
        BoundSql boundSql = criteria.where(criteria.getPkField(getMappingHandler()), new Object[] { id }).build(true,
            getMappingHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = jdbcTemplate.query(boundSql.getSql(), this.getRowMapper(clazz), boundSql.getParameters()
            .toArray());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T get(Criteria criteria, Long id) {
        BoundSql boundSql = criteria.where(criteria.getPkField(getMappingHandler()), new Object[] { id }).build(true,
            getMappingHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = (List<T>) jdbcTemplate
            .query(boundSql.getSql(), this.getRowMapper(criteria.getEntityClass()), id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T querySingleResult(T entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).build(entity, true, getMappingHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }

    public <T> T querySingleResult(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getMappingHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }

    public <T> T queryObject(Criteria criteria) {
        final BoundSql boundSql = criteria.build(true, getMappingHandler());
        return (T) jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Object.class);
    }

    public <T> List<T> queryObjectList(Criteria criteria, Class<T> elementType) {
        final BoundSql boundSql = criteria.build(true, getMappingHandler());
        return jdbcTemplate.queryForList(boundSql.getSql(), elementType, boundSql.getParameters().toArray());
    }

    public Map<String, Object> queryRowMap(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getMappingHandler());
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParameters()
            .toArray());
        return mapList == null || mapList.isEmpty() ? null : mapList.iterator().next();
    }

    public List<Map<String, Object>> queryRowMapList(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getMappingHandler());
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParameters()
            .toArray());
        return mapList;
    }

    public Object queryObjectForSql(String refSql) {
        return this.queryObjectForSql(refSql, "", EMPTY_OBJECT_ARRAY);
    }

    public Object queryObjectForSql(String refSql, Object[] params) {
        return this.queryObjectForSql(refSql, "", params);
    }

    public Object queryObjectForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Object.class);
    }

    public Map<String, Object> querySingleResultForSql(String refSql) {
        return this.querySingleResultForSql(refSql, "", EMPTY_OBJECT_ARRAY);
    }

    public <T> T querySingleResultForSql(String refSql, Class<T> elementType) {
        Map<String, Object> map = this.querySingleResultForSql(refSql, "", EMPTY_OBJECT_ARRAY);
        return mapToBean(map, elementType);
    }

    public Map<String, Object> querySingleResultForSql(String refSql, Object[] params) {
        return this.querySingleResultForSql(refSql, "", params);
    }

    public <T> T querySingleResultForSql(String refSql, Object[] params, Class<T> elementType) {
        Map<String, Object> map = this.querySingleResultForSql(refSql, "", params);
        return mapToBean(map, elementType);
    }

    public Map<String, Object> querySingleResultForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        Map<String, Object> map = jdbcTemplate.queryForMap(boundSql.getSql(), boundSql.getParameters().toArray());
        return map;
    }

    public <T> T querySingleResultForSql(String refSql, String expectParamKey, Object[] params, Class<T> elementType) {
        Map<String, Object> map = this.querySingleResultForSql(refSql, expectParamKey, params);
        return mapToBean(map, elementType);
    }

    public List<Map<String, Object>> queryListForSql(String refSql) {
        return this.queryListForSql(refSql, "", EMPTY_OBJECT_ARRAY);
    }

    public <T> List<T> queryListForSql(String refSql, Class<T> elementType) {
        List<Map<String, Object>> mapList = this.queryListForSql(refSql, "", EMPTY_OBJECT_ARRAY);
        return mapToBean(mapList, elementType);
    }

    public List<Map<String, Object>> queryListForSql(String refSql, Object[] params) {
        return this.queryListForSql(refSql, "", params);
    }

    public <T> List<T> queryListForSql(String refSql, Object[] params, Class<T> elementType) {
        List<Map<String, Object>> mapList = this.queryListForSql(refSql, "", params);
        return mapToBean(mapList, elementType);
    }

    public List<Map<String, Object>> queryListForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParameters()
            .toArray());
        return mapList;
    }

    public <T> List<T> queryListForSql(String refSql, String expectParamKey, Object[] params, Class<T> elementType) {
        List<Map<String, Object>> mapList = this.queryListForSql(refSql, expectParamKey, params);
        return mapToBean(mapList, elementType);
    }

    public int updateForSql(String refSql) {
        return this.updateForSql(refSql, "", EMPTY_OBJECT_ARRAY);
    }

    public int updateForSql(String refSql, Object[] params) {
        return this.updateForSql(refSql, "", params);
    }

    public int updateForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

}
