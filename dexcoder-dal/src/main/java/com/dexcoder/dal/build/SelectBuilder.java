package com.dexcoder.dal.build;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.exceptions.JdbcAssistantException;
import com.dexcoder.dal.handler.NameHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public class SelectBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN     = "SELECT ";

    protected List<String>        includeFields;
    protected List<String>        excludeFields;
    protected List<AutoField>     funcAutoFields;
    protected SqlBuilder          whereBuilder;
    protected SqlBuilder          orderByBuilder;

    protected boolean             isFieldExclusion = false;
    protected boolean             isOrderBy        = true;

    public SelectBuilder() {
        includeFields = new ArrayList<String>();
        excludeFields = new ArrayList<String>();
        funcAutoFields = new ArrayList<AutoField>();
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
        } else if (type == AutoFieldType.FUNC) {
            this.isFieldExclusion = Boolean.valueOf(fieldOperator);
            this.isOrderBy = Boolean.valueOf(sqlOperator);
            AutoField autoField = buildAutoField(fieldName, sqlOperator, fieldOperator, type, value);
            this.funcAutoFields.add(autoField);
        } else {
            throw new JdbcAssistantException("不支持的字段设置类型");
        }
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        whereBuilder.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        super.mergeEntityFields(entity, AutoFieldType.WHERE, nameHandler, isIgnoreNull);
        whereBuilder.getFields().putAll(this.getFields());
        String tableName = nameHandler.getTableName(clazz, whereBuilder.getFields());
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        if (columnFields.isEmpty() && !isFieldExclusion) {
            this.fetchClassFields(clazz);
        }
        if (!funcAutoFields.isEmpty()) {
            for (AutoField autoField : funcAutoFields) {
                String nativeFieldName = tokenParse(autoField.getName(), clazz, nameHandler);
                sb.append(nativeFieldName).append(",");
            }
        }
        if (!isFieldExclusion) {
            for (String columnField : columnFields) {
                //白名单 黑名单
                if (!includeFields.isEmpty() && !includeFields.contains(columnField)) {
                    continue;
                } else if (!excludeFields.isEmpty() && excludeFields.contains(columnField)) {
                    continue;
                }
                String columnName = nameHandler.getColumnName(clazz, columnField);
                sb.append(applyColumnAlias(columnName));
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" FROM ").append(applyTableAlias(tableName)).append(" ");
        whereBuilder.setTableAlias(getTableAlias());
        BoundSql whereBoundSql = whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sb.append(whereBoundSql.getSql());
        if (isOrderBy) {
            orderByBuilder.setTableAlias(getTableAlias());
            BoundSql orderByBoundSql = orderByBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
            sb.append(orderByBoundSql.getSql());
        }
        return new CriteriaBoundSql(sb.toString(), whereBoundSql.getParameters());
    }

    /**
     * 提取class 字段
     *
     * @param clazz
     */
    protected void fetchClassFields(Class<?> clazz) {
        //ClassUtils已经使用了缓存，此处就不用了
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
