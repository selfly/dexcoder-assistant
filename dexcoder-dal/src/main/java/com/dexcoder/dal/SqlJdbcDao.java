package com.dexcoder.dal;

import java.util.List;
import java.util.Map;

/**
 * 执行自定义sql jdbcDao
 * 
 * Created by liyd on 2016-1-7.
 */
public interface SqlJdbcDao {

    /**
     * 根据sql查询单个结果
     *
     * @param refSql the ref sql
     * @return object
     */
    Object queryObjectForSql(String refSql);

    /**
     * 根据sql查询单个结果
     *
     * @param refSql
     * @param params the params
     * @return list
     */
    Object queryObjectForSql(String refSql, Object[] params);

    /**
     * 根据sql查询单个结果
     *
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     * @return list list
     */
    Object queryObjectForSql(String refSql, String expectParamKey, Object[] params);

    /**
     * 根据sql查询单个结果行的map
     *
     * @param refSql the ref sql
     * @return map map
     */
    Map<String, Object> querySingleRowMapForSql(String refSql);

    /**
     * 根据sql查询单个结果行的map
     *
     * @param refSql the ref sql
     * @param params the params
     * @return map map
     */
    Map<String, Object> querySingleRowMapForSql(String refSql, Object[] params);

    /**
     * 根据sql查询单个结果行的map
     *
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     * @return map
     */
    Map<String, Object> querySingleRowMapForSql(String refSql, String expectParamKey, Object[] params);

    /**
     * 根据sql查询行的map列表
     *
     * @param refSql
     * @return list
     */
    List<Map<String, Object>> queryRowMapListForSql(String refSql);

    /**
     * 根据sql查询行的map列表
     *
     * @param refSql
     * @param params the params
     * @return list
     */
    List<Map<String, Object>> queryRowMapListForSql(String refSql, Object[] params);

    /**
     * 根据sql查询行的map列表
     *
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     * @return list list
     */
    List<Map<String, Object>> queryRowMapListForSql(String refSql, String expectParamKey, Object[] params);

    /**
     * 执行sql
     *
     * @param refSql the ref sql
     */
    int updateForSql(String refSql);

    /**
     * 执行sql
     *
     * @param refSql the ref sql
     * @param params the params
     */
    int updateForSql(String refSql, Object[] params);

    /**
     * 执行sql
     *
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     */
    int updateForSql(String refSql, String expectParamKey, Object[] params);
}
