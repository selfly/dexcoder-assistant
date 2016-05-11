package com.dexcoder.dal;

import java.util.List;
import java.util.Map;

import com.dexcoder.dal.build.Criteria;

/**
 * jdbc操作dao
 *
 * Created by liyd on 3/3/15.
 */
public interface JdbcDao {

    /**
     * 插入一条记录 自动处理主键
     *
     * @param entity
     * @return
     */
    Long insert(Object entity);

    /**
     * 插入一条记录 自动处理主键
     *
     * @param criteria the criteria
     * @return long long
     */
    Long insert(Criteria criteria);

    /**
     * 保存一条记录，不处理主键
     *
     * @param entity
     */
    void save(Object entity);

    /**
     * 保存一条记录，不处理主键
     *
     * @param criteria the criteria
     */
    void save(Criteria criteria);

    /**
     * 根据Criteria更新
     *
     * @param criteria the criteria
     */
    int update(Criteria criteria);

    /**
     * 根据实体更新
     *
     * @param entity the entity
     */
    int update(Object entity);

    /**
     * 根据实体更新
     *
     * @param entity
     * @param isIgnoreNull 是否忽略null值的属性 默认true
     * @return
     */
    int update(Object entity, boolean isIgnoreNull);

    /**
     * 根据Criteria删除
     *
     * @param criteria the criteria
     */
    int delete(Criteria criteria);

    /**
     * 删除记录 此方法会以实体中不为空的字段为条件
     *
     * @param entity
     */
    int delete(Object entity);

    /**
     * 删除记录
     *
     * @param clazz the clazz
     * @param id the id
     */
    int delete(Class<?> clazz, Long id);

    /**
     * 按设置的条件查询
     *
     * @param <T>  the type parameter
     * @param criteria the criteria
     * @return list
     */
    <T> List<T> queryList(Criteria criteria);

    /**
     * 按设置的条件查询
     *
     * @param <T>  the type parameter
     * @param clazz the clazz
     * @return list
     */
    <T> List<T> queryList(Class<?> clazz);

    /**
     * 查询列表
     *
     * @param entity the entity
     * @return the list
     */
    <T> List<T> queryList(T entity);

    /**
     * 查询列表
     *
     * @param <T>  the type parameter
     * @param entity the entity
     * @param criteria the criteria
     * @return the list
     */
    <T> List<T> queryList(T entity, Criteria criteria);

    /**
     * 查询记录数
     *
     * @param clazz
     * @return
     */
    int queryCount(Class<?> clazz);

    /**
     * 查询记录数
     *
     * @param entity
     * @return
     */
    int queryCount(Object entity);

    /**
     * 查询记录数
     *
     * @param criteria the criteria
     * @return int int
     */
    int queryCount(Criteria criteria);

    /**
     * 查询记录数
     *
     * @param entity the entity
     * @param criteria the criteria
     * @return int int
     */
    int queryCount(Object entity, Criteria criteria);

    /**
     * 根据主键得到记录
     *
     * @param <T>  the type parameter
     * @param clazz the clazz
     * @param id the id
     * @return t
     */
    <T> T get(Class<T> clazz, Long id);

    /**
     * 查询单个记录
     *
     * @param <T>   the type parameter
     * @param entity the entity
     * @return t t
     */
    <T> T querySingleResult(T entity);

    /**
     * 查询单个记录
     *
     * @param <T>     the type parameter
     * @param criteria the criteria
     * @return t t
     */
    <T> T querySingleResult(Criteria criteria);

    /**
     * 查询单个对象,例如count(*) max(id)这类只有一个结果的sql
     * 
     * @param criteria
     * @param <T>
     * @return
     */
    <T> T queryObject(Criteria criteria);

    /**
     * 查询单个对你的列表，例如id列表，name列表
     * 
     * @param criteria
     * @param elementType
     * @param <T>
     * @return
     */
    <T> List<T> queryObjectList(Criteria criteria, Class<T> elementType);

    /**
     * 查询列表 例如使用函数后和列不排斥的情况
     * 
     * @param criteria
     * @return
     */
    Map<String, Object> queryRowMap(Criteria criteria);

    /**
     * 查询列表 例如使用函数后和列不排斥的情况
     *
     * @param criteria
     * @return
     */
    List<Map<String, Object>> queryRowMapList(Criteria criteria);

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
     * @return
     */
    Map<String, Object> querySingleResultForSql(String refSql);

    /**
     * 根据sql查询单个结果
     *
     * @param <T>  the type parameter
     * @param refSql the ref sql
     * @param elementType the element type
     * @return t
     */
    <T> T querySingleResultForSql(String refSql, Class<T> elementType);

    /**
     * 根据sql查询单个结果行的map
     *
     * @param refSql the ref sql
     * @param params the params
     * @return map map
     */
    Map<String, Object> querySingleResultForSql(String refSql, Object[] params);

    /**
     * 根据sql查询单个结果
     *
     * @param <T>  the type parameter
     * @param refSql the ref sql
     * @param params the params
     * @param elementType the element type
     * @return map map
     */
    <T> T querySingleResultForSql(String refSql, Object[] params, Class<T> elementType);

    /**
     * 根据sql查询单个结果行的map
     *
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     * @return map
     */
    Map<String, Object> querySingleResultForSql(String refSql, String expectParamKey, Object[] params);

    /**
     * 根据sql查询单个结果
     *
     * @param <T>  the type parameter
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     * @param elementType the element type
     * @return  t
     */
    <T> T querySingleResultForSql(String refSql, String expectParamKey, Object[] params, Class<T> elementType);

    /**
     * 根据sql查询行的map列表
     *
     * @param refSql
     * @return list
     */
    List<Map<String, Object>> queryListForSql(String refSql);

    /**
     * 根据sql查询行的map列表
     *
     * @param <T>  the type parameter
     * @param refSql the ref sql
     * @param elementType the element type
     * @return list list
     */
    <T> List<T> queryListForSql(String refSql, Class<T> elementType);

    /**
     * 根据sql查询行的map列表
     *
     * @param refSql
     * @param params the params
     * @return list
     */
    List<Map<String, Object>> queryListForSql(String refSql, Object[] params);

    /**
     * 根据sql查询行的map列表
     *
     * @param <T>  the type parameter
     * @param refSql the ref sql
     * @param params the params
     * @param elementType the element type
     * @return list list
     */
    <T> List<T> queryListForSql(String refSql, Object[] params, Class<T> elementType);

    /**
     * 根据sql查询行的map列表
     *
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     * @return list list
     */
    List<Map<String, Object>> queryListForSql(String refSql, String expectParamKey, Object[] params);

    /**
     * 根据sql查询行的map列表
     *
     * @param <T>  the type parameter
     * @param refSql the ref sql
     * @param expectParamKey 写sql时访问的参数变量名称
     * @param params the params
     * @param elementType the element type
     * @return list list
     */
    <T> List<T> queryListForSql(String refSql, String expectParamKey, Object[] params, Class<T> elementType);

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

    //    /**
    //     * 查询blob字段值
    //     *
    //     * @param clazz
    //     * @param fieldName
    //     * @param id
    //     * @return
    //     */
    //    public byte[] getBlobValue(Class<?> clazz, String fieldName, Long id);
}
