package com.dexcoder.dal.build;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.annotation.Column;
import com.dexcoder.dal.annotation.Transient;
import com.dexcoder.dal.exceptions.JdbcAssistantException;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public class SelectBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "SELECT ";

    protected SqlBuilder          whereBuilder;
    protected SqlBuilder          orderByBuilder;

    public SelectBuilder(Class<?> clazz) {
        super(clazz);
        metaTable = new MetaTable.Builder(metaTable).initColumnAutoFields().initExcludeFields().initIncludeFields()
            .initFuncAutoFields().build();
        whereBuilder = new WhereBuilder(clazz);
        orderByBuilder = new OrderByBuilder(clazz);
    }

    public void addField(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                         Object value) {
        if (type == AutoFieldType.INCLUDE) {
            metaTable.getIncludeFields().add(fieldName);
        } else if (type == AutoFieldType.EXCLUDE) {
            metaTable.getExcludeFields().add(fieldName);
        } else if (type == AutoFieldType.ORDER_BY_ASC) {
            orderByBuilder.addField(fieldName, logicalOperator, "ASC", type, value);
        } else if (type == AutoFieldType.ORDER_BY_DESC) {
            orderByBuilder.addField(fieldName, logicalOperator, "DESC", type, value);
        } else if (type == AutoFieldType.FUNC) {
            new MetaTable.Builder(metaTable).isFieldExclusion(Boolean.valueOf(fieldOperator)).isOrderBy(
                Boolean.valueOf(logicalOperator));
            AutoField autoField = new AutoField.Builder().name(fieldName).logicalOperator(logicalOperator)
                .fieldOperator(fieldOperator).type(type).value(value).build();
            metaTable.getFuncAutoFields().add(autoField);
        } else {
            throw new JdbcAssistantException("不支持的字段设置类型");
        }
    }

    public void addCondition(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        whereBuilder.addCondition(fieldName, logicalOperator, fieldOperator, type, value);
    }

    public BoundSql build(Object entity, boolean isIgnoreNull, MappingHandler mappingHandler) {
        metaTable = new MetaTable.Builder(metaTable).mappingHandler(mappingHandler).build();
        //构建到whereBuilder
        new MetaTable.Builder(whereBuilder.getMetaTable()).tableAlias(metaTable.getTableAlias())
            .entity(entity, isIgnoreNull).mappingHandler(mappingHandler).build();
        //表名从whereBuilder获取
        String tableName = whereBuilder.getMetaTable().getTableAndAliasName();
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        if (!metaTable.hasColumnFields() && !metaTable.isFieldExclusion()) {
            this.fetchClassFields(metaTable.getTableClass());
        }
        if (metaTable.hasFuncAutoField()) {
            for (AutoField autoField : metaTable.getFuncAutoFields()) {
                String nativeFieldName = tokenParse(autoField, metaTable);
                sb.append(nativeFieldName).append(",");
            }
        }
        if (!metaTable.isFieldExclusion()) {
            for (AutoField columnAutoField : metaTable.getColumnAutoFields()) {
                //白名单 黑名单
                if (!metaTable.isIncludeField(columnAutoField.getName())) {
                    continue;
                } else if (metaTable.isExcludeField(columnAutoField.getName())) {
                    continue;
                }
                String columnName = metaTable.getColumnAndTableAliasName(columnAutoField);
                sb.append(columnName);
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" FROM ").append(tableName);
        BoundSql whereBoundSql = whereBuilder.build(entity, isIgnoreNull, mappingHandler);
        sb.append(whereBoundSql.getSql());
        if (metaTable.isOrderBy()) {
            new MetaTable.Builder(orderByBuilder.getMetaTable()).tableAlias(metaTable.getTableAlias()).build();
            BoundSql orderByBoundSql = orderByBuilder.build(entity, isIgnoreNull, mappingHandler);
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
        List<AutoField> columnAutoFields = new ArrayList<AutoField>();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Transient aTransient = readMethod.getAnnotation(Transient.class);
            if (aTransient != null) {
                continue;
            }
            String fieldAnnotationName = null;
            Column aColumn = readMethod.getAnnotation(Column.class);
            if (aColumn != null) {
                fieldAnnotationName = aColumn.name();
            }
            AutoField autoField = new AutoField.Builder().name(pd.getName()).annotationName(fieldAnnotationName)
                .build();
            columnAutoFields.add(autoField);
        }
        metaTable.getColumnAutoFields().addAll(columnAutoFields);
    }

}
