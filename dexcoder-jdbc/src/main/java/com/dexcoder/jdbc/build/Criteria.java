package com.dexcoder.jdbc.build;


import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;

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

    /**
     * sql Builder
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
     * 查询初始化
     *
     * @param clazz
     * @return
     */
    public static Criteria select(Class<?> clazz) {
        return new Criteria(clazz, new SelectBuilder());
    }

    public static Criteria insert(Class<?> clazz) {
        return new Criteria(clazz, new InsertBuilder());
    }

    public static Criteria update(Class<?> clazz) {
        return new Criteria(clazz, new UpdateBuilder());
    }

    public static Criteria delete(Class<?> clazz) {
        return new Criteria(clazz, new DeleteBuilder());
    }

    public Criteria ofField(String fieldName, Object value) {
        return new Criteria(this.entityClass, new FieldBuilder()).and(fieldName, value);
    }

//    /**
//     * 合并属性
//     *
//     * @param entity
//     */
//    public BoundSql mergeFields(Object entity, SqlCommandType sqlCommandType, boolean isSkipNull) {
//        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(entity.getClass());
//        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
//        for (PropertyDescriptor pd : propertyDescriptors) {
//            Method readMethod = pd.getReadMethod();
//            if (readMethod == null) {
//                continue;
//            }
//            Object value = ClassUtils.invokeMethod(readMethod, entity);
//            if (sqlCommandType == SqlCommandType.INSERT) {
//                if (value != null) {
//                    updateBuilder.set(pd.getName(), value);
//                }
//            } else if (sqlCommandType == SqlCommandType.UPDATE) {
//                if (!isSkipNull) {
//                    updateBuilder.set(pd.getName(), value);
//                } else if (value != null) {
//                    updateBuilder.set(pd.getName(), value);
//                }
//            } else if (sqlCommandType == SqlCommandType.SELECT) {
//
//            }
//
//
//        }
//    }

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
        this.sqlBuilder.addCondition(fieldName, null, fieldOperator, AutoFieldType.WHERE, value);
        return this;
    }

//    /**
//     * and 操作符
//     *
//     * @return
//     */
//    public Criteria and() {
//        this.and(null, null, null);
//        return this;
//    }

    /**
     * 设置and条件
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria and(String fieldName, Object... values) {
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

//    public Criteria or() {
//        this.or(null, null, null);
//        return this;
//    }

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
    public Criteria begin(String sqlOperator) {
        this.sqlBuilder.addCondition("(", sqlOperator, null, AutoFieldType.BRACKET_BEGIN, null);
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

    public BoundSql build(Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        return this.sqlBuilder.build(this.entityClass, entity, isIgnoreNull, nameHandler);
    }
}
