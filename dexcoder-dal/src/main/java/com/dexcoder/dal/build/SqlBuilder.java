package com.dexcoder.dal.build;

import java.util.Map;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.NameHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public interface SqlBuilder {

    /**
     * 添加操作字段
     *
     * @param fieldName     the field name
     * @param sqlOperator   the sql operator
     * @param fieldOperator the field operator
     * @param type          the type
     * @param value         the value
     */
    void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value);

    /**
     * 添加操作条件
     *
     * @param fieldName the field name
     * @param sqlOperator the sql operator
     * @param fieldOperator the field operator
     * @param type the type
     * @param value the value
     */
    void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value);

    /**
     * 设置表别名
     * 
     * @param alias
     */
    void setTableAlias(String alias);

    /**
     * 获取表别名
     * 
     * @return
     */
    String getTableAlias();

    /**
     * 是否拥有操作字段
     * 
     * @return
     */
    boolean hasFields();

    /**
     * 是否拥有某个字段
     * 
     * @param fieldName
     * @return
     */
    boolean hasField(String fieldName);

    /**
     * 获取所有操作字段
     * 
     * @return
     */
    Map<String, AutoField> getFields();

    /**
     * 构建BoundSql
     *
     * @param clazz the clazz
     * @param entity the entity
     * @param isIgnoreNull the is ignore null
     * @param nameHandler the name handler
     * @return bound sql
     */
    BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler);

}
