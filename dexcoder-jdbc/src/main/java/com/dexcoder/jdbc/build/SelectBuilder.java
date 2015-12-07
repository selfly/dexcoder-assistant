package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;
import com.dexcoder.jdbc.exceptions.JdbcAssistantException;
import com.dexcoder.jdbc.utils.ClassUtils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyd on 2015-12-4.
 */
public class SelectBuilder extends AbstractFieldBuilder {

    private static final String COMMAND_OPEN = "SELECT ";

    private List<String> includeFields;
    private List<String> excludeFields;
    private FieldBuilder whereBuilder;
    private FieldBuilder orderByBuilder;

    public SelectBuilder() {
        includeFields = new ArrayList<String>();
        excludeFields = new ArrayList<String>();
        whereBuilder = new WhereBuilder();
        orderByBuilder = new OrderByBuilder();
    }

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        if (type == AutoFieldType.INCLUDE) {
            includeFields.add(fieldName);
        } else if (type == AutoFieldType.EXCLUDE) {
            excludeFields.add(fieldName);
        } else if (type == AutoFieldType.ORDER_BY_ASC) {
            orderByBuilder.addField(fieldName, sqlOperator, "ASC", type, value);
        } else if (type == AutoFieldType.ORDER_BY_DESC) {
            orderByBuilder.addField(fieldName, sqlOperator, "DESC", type, value);
        } else {
            throw new JdbcAssistantException("不支持的字段设置类型");
        }
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        whereBuilder.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        super.mergeEntityFields(clazz, AutoFieldType.WHERE, nameHandler);
        whereBuilder.getFields().putAll(this.getFields());
        if (columnFields.isEmpty()) {
            this.fetchClassFields(clazz);
        }
        String tableName = nameHandler.getTableName(clazz, whereBuilder.getFields());
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        for (String columnField : columnFields) {
            //白名单 黑名单
            if (!includeFields.isEmpty() && !includeFields.contains(columnField)) {
                continue;
            } else if (!excludeFields.isEmpty() && excludeFields.contains(columnField)) {
                continue;
            }
            String columnName = nameHandler.getColumnName(columnField);
            sb.append(columnName);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" FROM ").append(tableName).append(" ");
        BoundSql whereBoundSql = whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sb.append(whereBoundSql.getSql());
        BoundSql orderByBoundSql = orderByBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sb.append(orderByBoundSql.getSql());
        return new CriteriaBoundSql(sb.toString(), whereBoundSql.getParameters());
    }

    /**
     * 提取class 字段
     *
     * @param clazz
     */
    private void fetchClassFields(Class<?> clazz) {
        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(clazz);
        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            columnFields.add(pd.getName());
        }
    }

}
