package com.dexcoder.assistant.persistence;

import java.util.List;

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
    public Long insert(Object entity);

    /**
     * 插入一条记录 自动处理主键
     *
     * @param criteria the criteria
     * @return long long
     */
    public Long insert(Criteria criteria);

    /**
     * 保存一条记录，不处理主键
     *
     * @param entity
     */
    public void save(Object entity);

    /**
     * 保存一条记录，不处理主键
     *
     * @param criteria the criteria
     */
    public void save(Criteria criteria);

    /**
     * 根据Criteria更新
     *
     * @param criteria the criteria
     */
    public void update(Criteria criteria);

    /**
     * 根据实体更新
     *
     * @param entity the entity
     */
    public void update(Object entity);

    /**
     * 根据Criteria删除
     *
     * @param criteria the criteria
     */
    public void delete(Criteria criteria);

    /**
     * 删除记录 此方法会以实体中不为空的字段为条件
     *
     * @param entity
     */
    public void delete(Object entity);

    /**
     * 删除记录
     *
     * @param clazz the clazz
     * @param id the id
     */
    public void delete(Class<?> clazz, Long id);

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

    /**
     * 查询blob字段值
     *
     * @param clazz
     * @param fieldName
     * @param id
     * @return
     */
    public byte[] getBlobValue(Class<?> clazz, String fieldName, Long id);
}
