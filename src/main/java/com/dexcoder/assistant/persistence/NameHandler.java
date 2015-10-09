package com.dexcoder.assistant.persistence;

/**
 * 名称处理接口
 * 
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public interface NameHandler {

    /**
     * 根据实体名获取表名
     *
     * @param entityClass
     * @return
     */
    public String getTableName(Class<?> entityClass);

    /**
     * 根据表名获取主键名
     * 
     * @param entityClass
     * @return
     */
    public String getPKName(Class<?> entityClass);

    /**
     * 根据属性名获取列名
     *
     * @param fieldName
     * @return
     */
    public String getColumnName(String fieldName);

    /**
     * 根据实体名获取主键序列名 oracle才有用 自增类主键数据库直接返回null即可
     *
     * @param entityClass the entity class
     * @param dialect the dialect
     * @return pK value
     */
    public String getPKValue(Class<?> entityClass, String dialect);
}
