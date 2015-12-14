package com.dexcoder.jdbc;

import java.util.List;
import java.util.Map;

import com.dexcoder.jdbc.build.Criteria;

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
     * 删除所有记录(TRUNCATE ddl权限)
     *
     * @param clazz the clazz
     */
    public void deleteAll(Class<?> clazz);

    /**
     * 按设置的条件查询
     *
     * @param <T>  the type parameter
     * @param criteria the criteria
     * @return list
     */
    public <T> List<T> queryList(Criteria criteria);

    /**
     * 按设置的条件查询
     *
     * @param <T>  the type parameter
     * @param clazz the clazz
     * @return list
     */
    public <T> List<T> queryList(Class<?> clazz);

    /**
     * 查询列表
     *
     * @param entity the entity
     * @return the list
     */
    public <T> List<T> queryList(T entity);

    /**
     * 查询列表
     *
     * @param <T>  the type parameter
     * @param entity the entity
     * @param criteria the criteria
     * @return the list
     */
    public <T> List<T> queryList(T entity, Criteria criteria);

    /**
     * 查询记录数
     *
     * @param entity
     * @return
     */
    public int queryCount(Object entity);

    /**
     * 查询记录数
     *
     * @param criteria the criteria
     * @return int int
     */
    public int queryCount(Criteria criteria);

    /**
     * 查询记录数
     *
     * @param entity the entity
     * @param criteria the criteria
     * @return int int
     */
    public int queryCount(Object entity, Criteria criteria);

    /**
     * 根据主键得到记录
     *
     * @param <T>  the type parameter
     * @param clazz the clazz
     * @param id the id
     * @return t
     */
    public <T> T get(Class<T> clazz, Long id);

    /**
     * 根据主键得到记录
     *
     * @param <T>  the type parameter
     * @param criteria the criteria
     * @param id the id
     * @return t
     */
    public <T> T get(Criteria criteria, Long id);

    /**
     * 查询单个记录
     *
     * @param <T>   the type parameter
     * @param entity the entity
     * @return t t
     */
    public <T> T querySingleResult(T entity);

    /**
     * 查询单个记录
     *
     * @param <T>     the type parameter
     * @param criteria the criteria
     * @return t t
     */
    public <T> T querySingleResult(Criteria criteria);

    Object queryForObject(Criteria criteria);

    /**
     * 根据sql查询
     *
     * @param refSql
     * @return list
     */
    List<Map<String, Object>> queryForSql(String refSql);

    /**
     * 根据sql查询
     *
     * @param refSql
     * @param params the params
     * @return list
     */
    List<Map<String, Object>> queryForSql(String refSql, Object[] params);

    /**
     * 根据sql查询
     *
     * @param refSql the ref sql
     * @param name 写sql时访问的参数变量名称
     * @param params the params
     * @return list list
     */
    List<Map<String, Object>> queryForSql(String refSql, String name, Object[] params);

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
     * @param expectParamKey the expect param key
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
