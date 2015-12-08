package com.dexcoder.jdbc.build;

/**
 * 组装sql时的列信息
 * <p/>
 * Created by liyd on 7/7/14.
 */
public class AutoField {

    /**
     * 名称
     */
    private String name;

    /**
     * 操作符 and or
     */
    private String sqlOperator;

    /**
     * 本身操作符 值大于、小于、in等
     */
    private String fieldOperator;

    /**
     * 值
     */
    private Object value;

    /**
     * 类型
     */
    private AutoFieldType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public AutoFieldType getType() {
        return type;
    }

    public void setType(AutoFieldType type) {
        this.type = type;
    }

    public String getSqlOperator() {
        return sqlOperator;
    }

    public void setSqlOperator(String sqlOperator) {
        this.sqlOperator = sqlOperator;
    }

    public String getFieldOperator() {
        return fieldOperator;
    }

    public void setFieldOperator(String fieldOperator) {
        this.fieldOperator = fieldOperator;
    }
}
