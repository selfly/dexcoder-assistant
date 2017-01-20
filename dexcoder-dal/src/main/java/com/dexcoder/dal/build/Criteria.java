package com.dexcoder.dal.build;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * sql操作Criteria
 * <p/>
 * Created by liyd on 3/3/15.
 */
public class Criteria<T> {

    /**
     * 操作的实体类
     */
    private Class<T>   entityClass;

    /**
     * build Builder
     */
    private SqlBuilder sqlBuilder;

    /**
     * constructor
     *
     * @param clazz      the clazz
     * @param sqlBuilder the field builder
     */
    private Criteria(Class<T> clazz, SqlBuilder sqlBuilder) {
        this.entityClass = clazz;
        this.sqlBuilder = sqlBuilder;
    }

    /**
     * select init
     *
     * @param clazz
     * @return
     */
    public static <T> Criteria<T> select(Class<T> clazz) {
        return new Criteria<T>(clazz, new SelectBuilder(clazz));
    }

    /**
     * insert init
     * 
     * @param clazz
     * @return
     */
    public static <T> Criteria<T> insert(Class<T> clazz) {
        return new Criteria<T>(clazz, new InsertBuilder(clazz));
    }

    /**
     * update init
     * 
     * @param clazz
     * @return
     */
    public static <T> Criteria<T> update(Class<T> clazz) {
        return new Criteria<T>(clazz, new UpdateBuilder(clazz));
    }

    /**
     * delete init
     * @param clazz
     * @return
     */
    public static <T> Criteria<T> delete(Class<T> clazz) {
        return new Criteria<T>(clazz, new DeleteBuilder(clazz));
    }

    /**
     * 设置mappingHandler
     * 
     * @param mappingHandler
     * @return
     */
    public Criteria<T> mappingHandler(MappingHandler mappingHandler) {
        this.sqlBuilder.getMetaTable().mappingHandler(mappingHandler);
        return this;
    }

    /**
     * 设置表别名
     * 
     * @param alias
     * @return
     */
    public Criteria<T> tableAlias(String alias) {
        this.sqlBuilder.getMetaTable().tableAlias(alias);
        return this;
    }

    /**
     * 添加白名单
     *
     * @param field
     * @return
     */
    public Criteria<T> include(String... field) {
        for (String f : field) {
            this.sqlBuilder.addField(f, null, null, AutoFieldType.INCLUDE, null);
        }
        return this;
    }

    /**
     * 添加黑名单
     *
     * @param field
     * @return
     */
    public Criteria<T> exclude(String... field) {
        for (String f : field) {
            this.sqlBuilder.addField(f, null, null, AutoFieldType.EXCLUDE, null);
        }
        return this;
    }

    /**
     * asc 排序属性
     *
     * @param field the field
     * @return
     */
    public Criteria<T> asc(String... field) {
        for (String f : field) {
            this.sqlBuilder.addField(f, null, "ASC", AutoFieldType.ORDER_BY_ASC, null);
        }
        return this;
    }

    /**
     * desc 排序属性
     *
     * @param field the field
     * @return
     */
    public Criteria<T> desc(String... field) {
        for (String f : field) {
            this.sqlBuilder.addField(f, null, "DESC", AutoFieldType.ORDER_BY_DESC, null);
        }
        return this;
    }

    /**
     * insert into属性
     *
     * @param fieldName
     * @param value
     * @return
     */
    public Criteria<T> into(String fieldName, Object value) {
        this.sqlBuilder.addField(fieldName, null, null, AutoFieldType.INSERT, value);
        return this;
    }

    /**
     * 设置操作属性
     *
     * @param fieldName the field name
     * @param value     the value
     * @return
     */
    public Criteria<T> set(String fieldName, Object value) {
        this.sqlBuilder.addField(fieldName, null, null, AutoFieldType.UPDATE, value);
        return this;
    }

    /**
     * 设置where条件属性
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria<T> where(String fieldName, Object[] values) {
        this.where(fieldName, "=", values);
        return this;
    }

    /**
     * 设置where条件属性
     *
     * @param fieldName     the field name
     * @param fieldOperator the operator
     * @param values         the values
     * @return
     */
    public Criteria<T> where(String fieldName, String fieldOperator, Object[] values) {
        this.sqlBuilder.addCondition(fieldName, null, fieldOperator, AutoFieldType.WHERE, values);
        return this;
    }

    /**
     * 设置and条件
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria<T> and(String fieldName, Object[] values) {
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
    public Criteria<T> and(String fieldName, String fieldOperator, Object[] values) {
        this.sqlBuilder.addCondition(fieldName, "and", fieldOperator, AutoFieldType.WHERE, values);
        return this;
    }

    /**
     * 设置or条件
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria<T> or(String fieldName, Object[] values) {
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
    public Criteria<T> or(String fieldName, String fieldOperator, Object[] values) {
        this.sqlBuilder.addCondition(fieldName, "or", fieldOperator, AutoFieldType.WHERE, values);
        return this;
    }

    /**
     * 开始左括号
     *
     * @return
     */
    public Criteria<T> begin() {
        this.begin("and");
        return this;
    }

    /**
     * 开始左括号
     *
     * @return
     */
    public Criteria<T> begin(String logicalOperator) {
        this.sqlBuilder.addCondition("(", logicalOperator, null, AutoFieldType.BRACKET_BEGIN, null);
        return this;
    }

    /**
     * 右括号结束
     *
     * @return
     */
    public Criteria<T> end() {
        this.sqlBuilder.addCondition(")", null, null, AutoFieldType.BRACKET_END, null);
        return this;
    }

    /**
     * 添加函数
     * 
     * @param func
     * @return
     */
    public Criteria<T> addSelectFunc(String func) {
        this.addSelectFunc(func, true, false);
        return this;
    }

    /**
     * 添加函数
     * 
     * @param func 函数代码
     * @param isFieldExclusion 是否与列互斥 默认true
     * @param isOrderBy 是否需要排序 默认fasle
     * @return
     */
    public Criteria<T> addSelectFunc(String func, boolean isFieldExclusion, boolean isOrderBy) {
        this.addSelectFunc(func, isFieldExclusion, isOrderBy, false);
        return this;
    }

    /**
     * 添加函数
     *
     * @param func 函数代码
     * @param isFieldExclusion 是否与列互斥 默认true
     * @param isOrderBy 是否需要排序 默认false
     * @param isOnlyOne 是否只使用一次(用完就删,例如jdbcDao的queryCount方法使用,保证用后不影响下一次criteria的使用)
     * @return criteria
     */
    public Criteria<T> addSelectFunc(String func, boolean isFieldExclusion, boolean isOrderBy, boolean isOnlyOne) {
        this.sqlBuilder.addField(func, String.valueOf(isOrderBy), String.valueOf(isFieldExclusion), AutoFieldType.FUNC,
            isOnlyOne);
        return this;
    }

    /**
     * 将设置的信息构建成BoundSql
     *
     * @param entity
     * @param isIgnoreNull
     * @return
     */
    public BoundSql build(Object entity, boolean isIgnoreNull) {
        return this.sqlBuilder.build(entity, isIgnoreNull);
    }

    /**
     * 将设置的信息构建成BoundSql
     *
     * @param isIgnoreNull
     * @return
     */
    public BoundSql build(boolean isIgnoreNull) {
        return build(null, isIgnoreNull);
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String getPkField() {
        return this.sqlBuilder.getMetaTable().getPkFieldName();
    }

    public String getColumnName(String fieldName) {
        return this.sqlBuilder.getMetaTable().getColumnName(fieldName);
    }
}
