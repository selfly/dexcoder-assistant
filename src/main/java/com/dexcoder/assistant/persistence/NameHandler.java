package com.dexcoder.assistant.persistence;

import java.util.Map;

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
     * @param entityClass the entity class
     * @param fieldMap the field map
     * @return table name
     */
    String getTableName(Class<?> entityClass, Map<String, AutoField> fieldMap);

    /**
     * 根据表名获取主键名
     *
     * @param entityClass the entity class
     * @return pK name
     */
    String getPKName(Class<?> entityClass);

    /**
     * 根据属性名获取列名
     *
     * @param fieldName
     * @return
     */
    String getColumnName(String fieldName);

    /**
     * 根据实体名获取主键序列名 oracle才有用 自增类主键数据库直接返回null即可
     *
     * @param entityClass the entity class
     * @param dialect the dialect
     * @return pK value
     */
    String getPKValue(Class<?> entityClass, String dialect);
}
