package com.dexcoder.dal.build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.NameHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public class UpdateBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "UPDATE ";

    /**
     * whereBuilder
     */
    private SqlBuilder            whereBuilder;

    public UpdateBuilder() {
        whereBuilder = new WhereBuilder();
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

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        metaTable = new MetaTable.Builder(metaTable).tableClass(clazz).entity(entity, isIgnoreNull)
            .nameHandler(nameHandler).build();
        //更新，主键都是在where
        AutoField pkAutoField = metaTable.getAutoFields().get(metaTable.getPkFieldName());
        if (pkAutoField != null) {
            metaTable.getAutoFields().remove(pkAutoField.getName());
            if (!whereBuilder.getMetaTable().hasAutoField(pkAutoField.getName())) {
                this.whereBuilder.addCondition(pkAutoField.getName(), pkAutoField.getLogicalOperator(),
                    pkAutoField.getFieldOperator(), pkAutoField.getType(), pkAutoField.getValue());
            }
        }
        //whereBuilder的metaTable
        new MetaTable.Builder(whereBuilder.getMetaTable()).tableClass(clazz).tableAlias(metaTable.getTableAlias())
            .nameHandler(nameHandler).build();

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
        BoundSql boundSql = whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sql.append(" ").append(boundSql.getSql());
        params.addAll(boundSql.getParameters());
        return new CriteriaBoundSql(sql.toString(), params);
    }
}
