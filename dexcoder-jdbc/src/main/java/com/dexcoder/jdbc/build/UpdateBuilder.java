package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.utils.AutoFieldUtils;

/**
 * Created by liyd on 2015-12-4.
 */
public class UpdateBuilder extends FieldBuilder {

    /**
     * 设置操作属性
     *
     * @param fieldName the field name
     * @param value     the value
     */
    public void set(String fieldName, Object value) {
        buildAutoField(fieldName, null, "=", AutoFieldType.UPDATE, value);
    }
}
