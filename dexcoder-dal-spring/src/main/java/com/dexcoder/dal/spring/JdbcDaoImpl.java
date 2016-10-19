package com.dexcoder.dal.spring;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.JdbcDao;
import com.dexcoder.dal.build.Criteria;
import com.dexcoder.dal.handler.KeyGenerator;

/**
 * jdbc操作dao
 * <p/>
 * Created by liyd on 3/3/15.
 */
@SuppressWarnings("unchecked")
public class JdbcDaoImpl extends AbstractJdbcDaoImpl implements JdbcDao {

    public <T> T insert(Serializable entity) {
        Criteria criteria = Criteria.insert(entity.getClass());
        return (T) this.insert(criteria, entity);
    }

    public <T> T insert(Criteria criteria) {
        return (T) this.insert(criteria, null);
    }

    public <T> T insert(Criteria criteria, Serializable entity) {
        criteria.mappingHandler(getMappingHandler());
        String pkFieldName = criteria.getPkField();
        final String pkColumn = criteria.getColumnName(pkFieldName);
        KeyGenerator keyGenerator = this.getKeyGenerator();
        Serializable pkValue = null;
        if (keyGenerator != null) {
            pkFieldName = keyGenerator.handlePkFieldName(pkFieldName, getDialect());
            pkValue = keyGenerator.generateKeyValue(criteria.getEntityClass(), getDialect());
            criteria.into(pkFieldName, pkValue);
        }

        final BoundSql boundSql = criteria.build(entity, true);
        if (keyGenerator == null || keyGenerator.isPkValueBySql()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(boundSql.getSql(), new String[] { pkColumn });
                    ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(boundSql.getParameters()
                        .toArray());
                    pss.setValues(ps);
                    return ps;
                }
            }, keyHolder);
            return (T) (Long) keyHolder.getKey().longValue();
        } else {
            jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
            return (T) pkValue;
        }
    }

    public void save(Serializable entity) {
        final BoundSql boundSql = Criteria.insert(entity.getClass()).mappingHandler(getMappingHandler())
            .build(entity, true);
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public void save(Criteria criteria) {
        final BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Serializable entity) {
        BoundSql boundSql = Criteria.update(entity.getClass()).mappingHandler(getMappingHandler()).build(entity, true);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Serializable entity, boolean isIgnoreNull) {
        BoundSql boundSql = Criteria.update(entity.getClass()).mappingHandler(getMappingHandler())
            .build(entity, isIgnoreNull);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Serializable entity) {
        BoundSql boundSql = Criteria.delete(entity.getClass()).mappingHandler(getMappingHandler()).build(entity, true);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Class<?> clazz, Serializable id) {
        Criteria criteria = Criteria.delete(clazz).mappingHandler(getMappingHandler());
        BoundSql boundSql = criteria.where(criteria.getPkField(), new Object[] { id }).build(true);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public <T> List<T> queryList(Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        return (List<T>) list;
    }

    public <T> List<T> queryList(Class<?> clazz) {
        BoundSql boundSql = Criteria.select(clazz).mappingHandler(getMappingHandler()).build(true);
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(clazz));
        return (List<T>) list;
    }

    public <T> List<T> queryList(T entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).mappingHandler(getMappingHandler()).build(entity, true);
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    public <T> List<T> queryList(T entity, Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(entity, true);
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    public int queryCount(Class<?> clazz) {
        BoundSql boundSql = Criteria.select(clazz).addSelectFunc("count(*)").mappingHandler(getMappingHandler())
            .build(null, true);
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public int queryCount(Serializable entity, Criteria criteria) {
        BoundSql boundSql = criteria.addSelectFunc("count(*)", true, false, true).mappingHandler(getMappingHandler())
            .build(entity, true);
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public int queryCount(Serializable entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).mappingHandler(getMappingHandler())
            .addSelectFunc("count(*)").build(entity, true);
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public int queryCount(Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).addSelectFunc("count(*)", true, false, true)
            .build(true);
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Integer.class);
    }

    public <T> T get(Class<T> clazz, Serializable id) {
        Criteria criteria = Criteria.select(clazz).mappingHandler(getMappingHandler());
        BoundSql boundSql = criteria.where(criteria.getPkField(), new Object[] { id }).build(true);
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = jdbcTemplate.query(boundSql.getSql(), this.getRowMapper(clazz), boundSql.getParameters()
            .toArray());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T get(Criteria criteria, Serializable id) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler())
            .where(criteria.getPkField(), new Object[] { id }).build(true);
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = (List<T>) jdbcTemplate
            .query(boundSql.getSql(), this.getRowMapper(criteria.getEntityClass()), id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T querySingleResult(T entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).mappingHandler(getMappingHandler()).build(entity, true);
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = (List<T>) jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        return DataAccessUtils.singleResult(list);
    }

    public <T> T querySingleResult(Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = (List<T>) jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        return DataAccessUtils.singleResult(list);
    }

    public <T> T queryObject(Criteria criteria) {
        final BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        return (T) jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Object.class);
    }

    public <T> List<T> queryObjectList(Criteria criteria, Class<T> elementType) {
        final BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        return jdbcTemplate.queryForList(boundSql.getSql(), elementType, boundSql.getParameters().toArray());
    }

    public <T> List<T> queryObjectList(Criteria criteria, Serializable entity, Class<T> elementType) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(entity, true);
        return jdbcTemplate.queryForList(boundSql.getSql(), elementType, boundSql.getParameters().toArray());
    }

    public Map<String, Object> queryRowMap(Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParameters()
            .toArray());
        return mapList == null || mapList.isEmpty() ? null : mapList.iterator().next();
    }

    public List<Map<String, Object>> queryRowMapList(Criteria criteria) {
        BoundSql boundSql = criteria.mappingHandler(getMappingHandler()).build(true);
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
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParameters()
            .toArray());
        return DataAccessUtils.singleResult(maps);
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
