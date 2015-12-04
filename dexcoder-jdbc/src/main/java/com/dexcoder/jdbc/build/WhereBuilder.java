package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-12-4.
 */
public class WhereBuilder extends FieldBuilder {

    /**
     * 是否设置过where
     */
    private boolean isWhere = false;

    /**
     * 设置where条件属性
     *
     * @param fieldName
     * @param value
     * @return
     */
    public void where(String fieldName, Object... value) {
        this.where(fieldName, "=", value);
    }

    /**
     * 设置where条件属性
     *
     * @param fieldName     the field name
     * @param fieldOperator the operator
     * @param value         the values
     * @return
     */
    public void where(String fieldName, String fieldOperator, Object... value) {
        if (this.isWhere) {
            throw new JdbcAssistantException("There can be only one 'where'!");
        }
        buildAutoField(fieldName, null, fieldOperator, AutoFieldType.WHERE, value);
        this.isWhere = true;
    }

    /**
     * and操作符
     */
    public void and() {
        this.hasWhere();
        buildAutoField("and", null, null, AutoFieldType.SQL_OPERATOR, null);
    }

    /**
     * 设置and条件
     *
     * @param fieldName
     * @param fieldOperator
     * @param values
     */
    public void and(String fieldName, String fieldOperator, Object[] values) {
        this.hasWhere();
        buildAutoField(fieldName, "and", fieldOperator, AutoFieldType.WHERE, values);
    }

    /**
     * or操作符
     */
    public void or() {
        this.hasWhere();
        buildAutoField("or", null, null, AutoFieldType.SQL_OPERATOR, null);
    }

    /**
     * 设置or条件
     *
     * @param fieldName
     * @param fieldOperator
     * @param values
     */
    public void or(String fieldName, String fieldOperator, Object[] values) {
        this.hasWhere();
        buildAutoField(fieldName, "or", fieldOperator, AutoFieldType.WHERE, values);
    }

    /**
     * 左括号开始
     */
    public void begin() {
        this.hasWhere();
        buildAutoField("(", null, null, AutoFieldType.BRACKET, null);
    }

    /**
     * 右括号结束
     */
    public void end() {
        this.hasWhere();
        buildAutoField(")", null, null, AutoFieldType.BRACKET, null);
    }

    /**
     * 检查是否使用了where
     */
    private void hasWhere() {
        if (!this.isWhere) {
            throw new JdbcAssistantException("使用and或or之前，请先使用where！");
        }
    }

}
