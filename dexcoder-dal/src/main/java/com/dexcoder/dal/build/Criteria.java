package com.dexcoder.dal.build;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * sql操作Criteria
 * <p/>
 * Created by liyd on 3/3/15.
 */
public class Criteria {

    /**
     * 操作的实体类
     */
    private Class<?>   entityClass;

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
    private Criteria(Class<?> clazz, SqlBuilder sqlBuilder) {
        this.entityClass = clazz;
        this.sqlBuilder = sqlBuilder;
    }

    /**
     * select init
     *
     * @param clazz
     * @return
     */
    public static Criteria select(Class<?> clazz) {
        return new Criteria(clazz, new SelectBuilder(clazz));
    }

    /**
     * insert init
     * 
     * @param clazz
     * @return
     */
    public static Criteria insert(Class<?> clazz) {
        return new Criteria(clazz, new InsertBuilder(clazz));
    }

    /**
     * update init
     * 
     * @param clazz
     * @return
     */
    public static Criteria update(Class<?> clazz) {
        return new Criteria(clazz, new UpdateBuilder(clazz));
    }

    /**
     * delete init
     * @param clazz
     * @return
     */
    public static Criteria delete(Class<?> clazz) {
        return new Criteria(clazz, new DeleteBuilder(clazz));
    }

    /**
     * 设置表别名
     * 
     * @param alias
     * @return
     */
    public Criteria tableAlias(String alias) {
        new MetaTable.Builder(this.sqlBuilder.getMetaTable()).tableAlias(alias);
        return this;
    }

    /**
     * 添加白名单
     *
     * @param field
     * @return
     */
    public Criteria include(String... field) {
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
    public Criteria exclude(String... field) {
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
    public Criteria asc(String... field) {
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
    public Criteria desc(String... field) {
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
    public Criteria into(String fieldName, Object value) {
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
    public Criteria set(String fieldName, Object value) {
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
    public Criteria where(String fieldName, Object[] values) {
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
    public Criteria where(String fieldName, String fieldOperator, Object[] values) {
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
        this.sqlBuilder.addCondition(fieldName, "or", fieldOperator, AutoFieldType.WHERE, values);
        return this;
    }

    /**
     * 开始左括号
     *
     * @return
     */
    public Criteria begin() {
        this.begin("and");
        return this;
    }

    /**
     * 开始左括号
     *
     * @return
     */
    public Criteria begin(String logicalOperator) {
        this.sqlBuilder.addCondition("(", logicalOperator, null, AutoFieldType.BRACKET_BEGIN, null);
        return this;
    }

    /**
     * 右括号结束
     *
     * @return
     */
    public Criteria end() {
        this.sqlBuilder.addCondition(")", null, null, AutoFieldType.BRACKET_END, null);
        return this;
    }

    /**
     * 添加函数
     * 
     * @param func
     * @return
     */
    public Criteria addSelectFunc(String func) {
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
    public Criteria addSelectFunc(String func, boolean isFieldExclusion, boolean isOrderBy) {
        this.sqlBuilder.addField(func, String.valueOf(isOrderBy), String.valueOf(isFieldExclusion), AutoFieldType.FUNC,
            null);
        return this;
    }

    /**
     * 将设置的信息构建成BoundSql
     *
     * @param entity
     * @param isIgnoreNull
     * @param mappingHandler
     * @return
     */
    public BoundSql build(Object entity, boolean isIgnoreNull, MappingHandler mappingHandler) {
        return this.sqlBuilder.build(entity, isIgnoreNull, mappingHandler);
    }

    /**
     * 将设置的信息构建成BoundSql
     *
     * @param isIgnoreNull
     * @param mappingHandler
     * @return
     */
    public BoundSql build(boolean isIgnoreNull, MappingHandler mappingHandler) {
        return build(null, isIgnoreNull, mappingHandler);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getPkField(MappingHandler mappingHandler) {
        return this.sqlBuilder.getMetaTable().getPkFieldName(mappingHandler);
    }
}
