package com.dexcoder.assistant.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dexcoder.assistant.exceptions.AssistantException;

/**
 * sql操作Criteria
 * 
 * Created by liyd on 3/3/15.
 */
public class Criteria {

    /** 操作的实体类 */
    private Class<?>        entityClass;

    /** 操作的字段 */
    private List<AutoField> autoFields;

    /** 排序字段 */
    private List<AutoField> orderByFields;

    /** 白名单 */
    private List<String>    includeFields;

    /** 黑名单 */
    private List<String>    excludeFields;

    /** where标识 */
    private boolean         isWhere = false;

    /**
     * constructor
     *
     * @param clazz
     */
    public Criteria(Class<?> clazz) {
        this.entityClass = clazz;
        this.autoFields = new ArrayList<AutoField>();
        this.orderByFields = new ArrayList<AutoField>();
    }

    /**
     * 初始化
     * 
     * @param clazz
     * @return
     */
    public static Criteria create(Class<?> clazz) {
        return new Criteria(clazz);
    }

    /**
     * 添加白名单
     *
     * @param field
     * @return
     */
    public Criteria include(String... field) {
        if (this.includeFields == null) {
            this.includeFields = new ArrayList<String>();
        }
        this.includeFields.addAll(Arrays.asList(field));
        return this;
    }

    /**
     * 添加黑名单
     *
     * @param field
     * @return
     */
    public Criteria exclude(String... field) {
        if (this.excludeFields == null) {
            this.excludeFields = new ArrayList<String>();
        }
        this.excludeFields.addAll(Arrays.asList(field));
        return this;
    }

    /**
     * asc 排序属性
     *
     * @param field the field
     * @return
     */
    public Criteria asc(String... field) {
        for (String f : field) {
            AutoField autoField = this.buildAutoFields(f, null, "ASC", AutoField.FIELD_ORDER_BY,
                null);
            this.orderByFields.add(autoField);
        }
        return this;
    }

    /**
     * desc 排序属性
     *
     * @param field the field
     * @return
     */
    public Criteria desc(String... field) {
        for (String f : field) {
            AutoField autoField = this.buildAutoFields(f, null, "DESC", AutoField.FIELD_ORDER_BY,
                null);
            this.orderByFields.add(autoField);
        }
        return this;
    }

    /**
     * 设置操作属性
     *
     * @param fieldName the field name
     * @param value the value
     * @return
     */
    public Criteria set(String fieldName, Object value) {
        AutoField autoField = this.buildAutoFields(fieldName, null, "=", AutoField.FIELD_UPDATE,
            value);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置主键值名称，如oracle序列名，非直接的值
     * 
     * @param pkName
     * @param valueName
     * @return
     */
    public Criteria setPKValueName(String pkName, String valueName) {
        AutoField autoField = this.buildAutoFields(pkName, null, "=", AutoField.PK_VALUE_NAME,
            valueName);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置and条件
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria and(String fieldName, Object[] values) {
        this.and(fieldName, "=", values);
        return this;
    }

    /**
     * 设置and条件
     *
     * @param fieldName
     * @param fieldOperator
     * @param values
     * @return
     */
    public Criteria and(String fieldName, String fieldOperator, Object[] values) {
        AutoField autoField = this.buildAutoFields(fieldName, "and", fieldOperator,
            AutoField.FIELD_WHERE, values);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置or条件
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria or(String fieldName, Object[] values) {
        this.or(fieldName, "=", values);
        return this;
    }

    /**
     * 设置or条件
     *
     * @param fieldName
     * @param fieldOperator
     * @param values
     * @return
     */
    public Criteria or(String fieldName, String fieldOperator, Object[] values) {
        AutoField autoField = this.buildAutoFields(fieldName, "or", fieldOperator,
            AutoField.FIELD_WHERE, values);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置where条件属性
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria where(String fieldName, Object[] values) {
        this.where(fieldName, "=", values);
        return this;
    }

    /**
     * 设置where条件属性
     *
     * @param fieldName the field name
     * @param fieldOperator the operator
     * @param values the values
     * @return
     */
    public Criteria where(String fieldName, String fieldOperator, Object[] values) {
        if (this.isWhere) {
            throw new AssistantException("There can be only one 'where'!");
        }
        AutoField autoField = this.buildAutoFields(fieldName, "and", fieldOperator,
            AutoField.FIELD_WHERE, values);
        this.autoFields.add(autoField);
        this.isWhere = true;
        return this;
    }

    /**
     * 开始左括号
     * 
     * @return
     */
    public Criteria beginBracket() {
        this.beginBracket("and");
        return this;
    }

    /**
     * 左括号开始
     *
     * @return
     */
    public Criteria beginBracket(String operator) {
        AutoField autoField = this.buildAutoFields("(", operator, null, AutoField.FIELD_BRACKET,
            null);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 右括号结束
     * 
     * @return
     */
    public Criteria endBracket() {
        AutoField autoField = this.buildAutoFields(")", null, null, AutoField.FIELD_BRACKET, null);
        this.autoFields.add(autoField);
        return this;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public List<AutoField> getAutoFields() {
        return autoFields;
    }

    public List<String> getIncludeFields() {
        return includeFields;
    }

    public List<String> getExcludeFields() {
        return excludeFields;
    }

    public List<AutoField> getOrderByFields() {
        return orderByFields;
    }

    /**
     * 获取操作的字段
     *
     * @return
     */
    private AutoField buildAutoFields(String fieldName, String sqlOperator, String fieldOperator,
                                      int type, Object... values) {
        AutoField autoField = new AutoField();
        autoField.setName(fieldName);
        autoField.setSqlOperator(sqlOperator);
        autoField.setFieldOperator(fieldOperator);
        autoField.setValues(values);
        autoField.setType(type);

        return autoField;
    }

}
