package com.dexcoder.dal.spring;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.JdbcDao;
import com.dexcoder.dal.build.AbstractSqlBuilder;
import com.dexcoder.dal.build.Criteria;
import com.dexcoder.dal.handler.NameHandler;

/**
 * jdbc操作dao
 * <p/>
 * Created by liyd on 3/3/15.
 */
@SuppressWarnings("unchecked")
public class JdbcDaoImpl extends SqlJdbcDaoImpl implements JdbcDao {

    public Long insert(Object entity) {
        NameHandler handler = this.getNameHandler();
        Criteria criteria = Criteria.insert(entity.getClass());
        String nativePKValue = handler.getPkNativeValue(entity.getClass(), getDialect());
        if (StrUtils.isNotBlank(nativePKValue)) {
            String pkFieldName = handler.getPkFieldName(entity.getClass());
            criteria.into(AbstractSqlBuilder.NATIVE_TOKENS[2] + pkFieldName + AbstractSqlBuilder.NATIVE_TOKENS[3],
                nativePKValue);
        }
        final BoundSql boundSql = criteria.build(entity, true, getNameHandler());
        return this.insert(boundSql, entity.getClass());
    }

    public Long insert(Criteria criteria) {
        final BoundSql boundSql = criteria.build(true, getNameHandler());
        return this.insert(boundSql, criteria.getEntityClass());
    }

    public void save(Object entity) {
        final BoundSql boundSql = Criteria.insert(entity.getClass()).build(entity, true, getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public void save(Criteria criteria) {
        final BoundSql boundSql = criteria.build(true, getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getNameHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Object entity) {
        BoundSql boundSql = Criteria.update(entity.getClass()).build(entity, true, getNameHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int update(Object entity, boolean isIgnoreNull) {
        BoundSql boundSql = Criteria.update(entity.getClass()).build(entity, isIgnoreNull, getNameHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getNameHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Object entity) {
        BoundSql boundSql = Criteria.delete(entity.getClass()).build(entity, true, getNameHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int delete(Class<?> clazz, Long id) {
        Criteria criteria = Criteria.delete(clazz);
        BoundSql boundSql = criteria.where(criteria.getPkField(getNameHandler()), new Object[] { id }).build(true,
            getNameHandler());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public <T> List<T> queryList(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getNameHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        return (List<T>) list;
    }

    public <T> List<T> queryList(Class<?> clazz) {
        BoundSql boundSql = Criteria.select(clazz).build(true, getNameHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(clazz));
        return (List<T>) list;
    }

    public <T> List<T> queryList(T entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).build(entity, true, getNameHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    public <T> List<T> queryList(T entity, Criteria criteria) {
        BoundSql boundSql = criteria.build(entity, true, getNameHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    public int queryCount(Class<?> clazz) {
        BoundSql boundSql = Criteria.select(clazz).addSelectFunc("count(*)").build(null, true, getNameHandler());
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int queryCount(Object entity, Criteria criteria) {
        BoundSql boundSql = criteria.addSelectFunc("count(*)").build(entity, true, getNameHandler());
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int queryCount(Object entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).addSelectFunc("count(*)")
            .build(entity, true, getNameHandler());
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public int queryCount(Criteria criteria) {
        BoundSql boundSql = criteria.addSelectFunc("count(*)").build(true, getNameHandler());
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParameters().toArray());
    }

    public <T> T get(Class<T> clazz, Long id) {
        Criteria criteria = Criteria.select(clazz);
        BoundSql boundSql = criteria.where(criteria.getPkField(getNameHandler()), new Object[] { id }).build(true,
            getNameHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = jdbcTemplate.query(boundSql.getSql(), this.getRowMapper(clazz), boundSql.getParameters()
            .toArray());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T get(Criteria criteria, Long id) {
        BoundSql boundSql = criteria.where(criteria.getPkField(getNameHandler()), new Object[] { id }).build(true,
            getNameHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = (List<T>) jdbcTemplate
            .query(boundSql.getSql(), this.getRowMapper(criteria.getEntityClass()), id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T querySingleResult(T entity) {
        BoundSql boundSql = Criteria.select(entity.getClass()).build(entity, true, getNameHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(entity.getClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }

    public <T> T querySingleResult(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getNameHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParameters().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }

    public <T> T queryObject(Criteria criteria) {
        final BoundSql boundSql = criteria.build(true, getNameHandler());
        return (T) jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Object.class);
    }

    public List<Map<String, Object>> queryRowMapList(Criteria criteria) {
        BoundSql boundSql = criteria.build(true, getNameHandler());
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParameters()
            .toArray());
        return convertMapKeyToCamel(mapList);
    }
}
