package com.dexcoder.dal.spring;

import java.util.List;
import java.util.Map;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.SqlJdbcDao;

/**
 * Created by liyd on 2016-1-7.
 */
public class SqlJdbcDaoImpl extends AbstractJdbcDaoImpl implements SqlJdbcDao {

    public Object queryObjectForSql(String refSql) {
        return this.queryObjectForSql(refSql, null, null);
    }

    public Object queryObjectForSql(String refSql, Object[] params) {
        return this.queryObjectForSql(refSql, null, params);
    }

    public Object queryObjectForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParameters().toArray(), Object.class);
    }

    public Map<String, Object> querySingleRowMapForSql(String refSql) {
        return this.querySingleRowMapForSql(refSql, null, null);
    }

    public Map<String, Object> querySingleRowMapForSql(String refSql, Object[] params) {
        return this.querySingleRowMapForSql(refSql, null, params);
    }

    public Map<String, Object> querySingleRowMapForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        Map<String, Object> map = jdbcTemplate.queryForMap(boundSql.getSql(), boundSql.getParameters().toArray());
        return convertMapKeyToCamel(map);
    }

    public List<Map<String, Object>> queryRowMapListForSql(String refSql) {
        return this.queryRowMapListForSql(refSql, null, null);
    }

    public List<Map<String, Object>> queryRowMapListForSql(String refSql, Object[] params) {
        return this.queryRowMapListForSql(refSql, null, params);
    }

    public List<Map<String, Object>> queryRowMapListForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParameters()
            .toArray());
        return convertMapKeyToCamel(mapList);
    }

    public int updateForSql(String refSql) {
        return this.updateForSql(refSql, null, null);
    }

    public int updateForSql(String refSql, Object[] params) {
        return this.updateForSql(refSql, null, params);
    }

    public int updateForSql(String refSql, String expectParamKey, Object[] params) {
        BoundSql boundSql = this.sqlFactory.getBoundSql(refSql, expectParamKey, params);
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParameters().toArray());
    }

}
