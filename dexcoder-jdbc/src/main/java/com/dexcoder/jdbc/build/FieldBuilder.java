package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;

import java.util.Map;

/**
 * Created by liyd on 2015-12-4.
 */
public interface FieldBuilder {

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

    void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value);

    boolean hasFields();

    Map<String, AutoField> getFields();

    BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler);


}
