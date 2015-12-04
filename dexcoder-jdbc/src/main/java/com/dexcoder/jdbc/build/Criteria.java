package com.dexcoder.jdbc.build;


import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.utils.ClassUtils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * sql操作Criteria
 * <p/>
 * Created by liyd on 3/3/15.
 */
public class Criteria {

    /**
     * 操作的实体类
     */
    private Class<?> entityClass;

    private SelectBuilder selectBuilder;

    private WhereBuilder whereBuilder;

    private OrderByBuilder orderByBuilder;

    private UpdateBuilder updateBuilder;

    /**
     * constructor
     *
     * @param clazz
     */
    private Criteria(Class<?> clazz) {
        this.entityClass = clazz;
        this.whereBuilder = new WhereBuilder();
        this.selectBuilder = new SelectBuilder();
        this.orderByBuilder = new OrderByBuilder();
        this.updateBuilder = new UpdateBuilder();
    }

    /**
     * 初始化
     *
     * @param clazz
     * @return
     */
    public static Criteria of(Class<?> clazz) {
        return new Criteria(clazz);
    }

    /**
     * 合并属性
     *
     * @param entity
     */
    public BoundSql mergeFields(Object entity, SqlCommandType sqlCommandType, boolean isSkipNull) {
        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(entity.getClass());
        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Object value = ClassUtils.invokeMethod(readMethod, entity);
            if (sqlCommandType == SqlCommandType.INSERT) {
                if (value != null) {
                    updateBuilder.set(pd.getName(), value);
                }
            } else if (sqlCommandType == SqlCommandType.UPDATE) {
                if (!isSkipNull) {
                    updateBuilder.set(pd.getName(), value);
                } else if (value != null) {
                    updateBuilder.set(pd.getName(), value);
                }
            } else if (sqlCommandType == SqlCommandType.SELECT) {

            }


        }
    }

    /**
     * 添加白名单
     *
     * @param field
     * @return
     */
    public Criteria include(String... field) {
        this.selectBuilder.include(field);
        return this;
    }

    /**
     * 添加黑名单
     *
     * @param field
     * @return
     */
    public Criteria exclude(String... field) {
        selectBuilder.exclude(field);
        return this;
    }

    /**
     * asc 排序属性
     *
     * @param field the field
     * @return
     */
    public Criteria asc(String... field) {
        orderByBuilder.asc(field);
        return this;
    }

    /**
     * desc 排序属性
     *
     * @param field the field
     * @return
     */
    public Criteria desc(String... field) {
        orderByBuilder.desc(field);
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
        updateBuilder.set(fieldName, value);
        return this;
    }

//    /**
//     * 设置主键值名称，如oracle序列名，非直接的值
//     *
//     * @param pkName
//     * @param valueName
//     * @return
//     */
//    public Criteria setPKValueName(String pkName, String valueName) {
//        AutoField autoField = AutoFieldUtils.buildAutoFields(pkName, null, "=",
//                AutoField.PK_VALUE_NAME, valueName);
//        this.autoFields.add(autoField);
//        return this;
//    }

    /**
     * 设置where条件属性
     *
     * @param fieldName
     * @param value
     * @return
     */
    public Criteria where(String fieldName, Object value) {
        this.where(fieldName, "=", value);
        return this;
    }

    /**
     * 设置where条件属性
     *
     * @param fieldName     the field name
     * @param fieldOperator the operator
     * @param value         the values
     * @return
     */
    public Criteria where(String fieldName, String fieldOperator, Object... value) {
        whereBuilder.where(fieldName, fieldOperator, value);
        return this;
    }

    /**
     * and 操作符
     *
     * @return
     */
    public Criteria and() {
        whereBuilder.and();
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
        whereBuilder.and(fieldName, fieldOperator, values);
        return this;
    }

    public Criteria or() {
        whereBuilder.or();
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
        whereBuilder.or(fieldName, fieldOperator, values);
        return this;
    }

    /**
     * 开始左括号
     *
     * @return
     */
    public Criteria begin() {
        whereBuilder.begin();
        return this;
    }

    /**
     * 右括号结束
     *
     * @return
     */
    public Criteria end() {
        whereBuilder.end();
        return this;
    }
}
