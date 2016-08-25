package com.dexcoder.dal.build;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public class UpdateBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "UPDATE ";

    /**
     * whereBuilder
     */
    private SqlBuilder            whereBuilder;

    public UpdateBuilder(Class<?> clazz) {
        super(clazz);
        whereBuilder = new WhereBuilder(clazz);
    }

    public void addField(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                         Object value) {
        AutoField autoField = new AutoField.Builder().name(fieldName).logicalOperator(logicalOperator)
            .fieldOperator(fieldOperator).type(type).value(value).build();
        metaTable.getAutoFields().put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        whereBuilder.addCondition(fieldName, logicalOperator, fieldOperator, type, value);
    }

    public BoundSql build(Object entity, boolean isIgnoreNull, MappingHandler mappingHandler) {
        metaTable = new MetaTable.Builder(metaTable).entity(entity, isIgnoreNull).mappingHandler(mappingHandler)
            .build();

        Iterator<Map.Entry<String, AutoField>> iterator = metaTable.getAutoFields().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AutoField> entry = iterator.next();
            AutoField pkAutoField = entry.getValue();
            if (StringUtils.equals(metaTable.getPkFieldName(), entry.getKey())) {
                iterator.remove();
                if (!whereBuilder.getMetaTable().hasAutoField(entry.getKey())) {
                    this.whereBuilder.addCondition(pkAutoField.getName(), pkAutoField.getLogicalOperator(),
                        pkAutoField.getFieldOperator(), pkAutoField.getType(), pkAutoField.getValue());
                }
            } else {
                this.whereBuilder.addCondition(pkAutoField.getName(), pkAutoField.getLogicalOperator(),
                    pkAutoField.getFieldOperator(), AutoFieldType.TRANSIENT, pkAutoField.getValue());
            }
        }

        //whereBuilder的metaTable
        new MetaTable.Builder(whereBuilder.getMetaTable()).tableAlias(metaTable.getTableAlias())
            .mappingHandler(mappingHandler).build();

        StringBuilder sql = new StringBuilder(COMMAND_OPEN);
        List<Object> params = new ArrayList<Object>();
        //tableName必须从whereBuilder中获取，以便水平分表时能正确获取表名
        sql.append(whereBuilder.getMetaTable().getTableAndAliasName()).append(" SET ");
        for (Map.Entry<String, AutoField> entry : metaTable.getAutoFields().entrySet()) {
            String columnName = metaTable.getColumnAndTableAliasName(entry.getValue());
            AutoField autoField = entry.getValue();
            if (autoField.isNativeField()) {
                String nativeFieldName = tokenParse(autoField.getName(), metaTable);
                String nativeValue = tokenParse(String.valueOf(autoField.getValue()), metaTable);
                sql.append(nativeFieldName).append(" = ").append(nativeValue).append(",");
            } else if (autoField.getValue() == null) {
                sql.append(columnName).append(" = NULL,");
            } else {
                sql.append(columnName).append(" = ?,");
                params.add(autoField.getValue());
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        BoundSql boundSql = whereBuilder.build(entity, isIgnoreNull, mappingHandler);
        sql.append(boundSql.getSql());
        params.addAll(boundSql.getParameters());
        return new CriteriaBoundSql(sql.toString(), params);
    }
}
