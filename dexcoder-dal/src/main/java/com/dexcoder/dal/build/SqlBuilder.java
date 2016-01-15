package com.dexcoder.dal.build;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public interface SqlBuilder {

    /**
     * 添加操作字段
     *
     * @param fieldName     the field name
     * @param logicalOperator   the logicalOperator
     * @param fieldOperator the field operator
     * @param type          the type
     * @param value         the value
     */
    void addField(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type, Object value);

    /**
     * 添加操作条件
     *
     * @param fieldName the field name
     * @param logicalOperator the logicalOperator
     * @param fieldOperator the field operator
     * @param type the type
     * @param value the value
     */
    void addCondition(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type, Object value);

    /**
     * 获取操作表对象
     *
     * @return
     */
    MetaTable getMetaTable();

    /**
     * 构建BoundSql
     *
     * @param entity 可以为空
     * @param isIgnoreNull entity不为空的情况下是否忽略null属性
     * @param mappingHandler 映射处理器，如果注解指定了单独的mappingHandler则会被覆盖
     * @return bound sql
     */
    BoundSql build(Object entity, boolean isIgnoreNull, MappingHandler mappingHandler);

}
