package com.dexcoder.dal.handler;

import java.util.Map;

import com.dexcoder.dal.build.AutoField;

/**
 * 名称处理接口
 * <p/>
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public interface MappingHandler {

    /**
     * 根据实体名获取表名
     *
     * @param entityClass the entity class
     * @param fieldMap    the field map
     * @return table name
     */
    String getTableName(Class<?> entityClass, Map<String, AutoField> fieldMap);

    /**
     * 根据类名获取主键字段名
     *
     * @param entityClass the entity class
     * @return pK name
     */
    String getPkFieldName(Class<?> entityClass);

    /**
     * 根据类名获取主键列名
     *
     * @param entityClass the entity class
     * @return pK name
     */
    String getPkColumnName(Class<?> entityClass);

    /**
     * 根据属性名获取列名
     *
     * @param entityClass the entity class
     * @param fieldName the field name
     * @return column name
     */
    String getColumnName(Class<?> entityClass, String fieldName);

    /**
     * 根据实体名获取主键序列名 oracle才有用 自增类主键数据库直接返回null即可
     *
     * @param entityClass the entity class
     * @param dialect     the dialect
     * @return pK value
     */
    String getPkNativeValue(Class<?> entityClass, String dialect);
}
