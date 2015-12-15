package com.dexcoder.dal.build;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.NameHandler;

import java.util.Map;

/**
 * Created by liyd on 2015-12-4.
 */
public interface SqlBuilder {

    /**
     * 添加操作字段
     *
     * @param fieldName     the field name
     * @param sqlOperator   the build operator
     * @param fieldOperator the field operator
     * @param type          the type
     * @param value         the value
     */
    void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value);

    void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value);

    boolean hasFields();

    boolean hasField(String fieldName);

    Map<String, AutoField> getFields();

    BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler);


}
