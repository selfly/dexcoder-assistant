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

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        autoTable.addAutoField(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        whereBuilder.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        autoTable.mergeEntityFields(entity, AutoFieldType.UPDATE, isIgnoreNull);
        String pkFieldName = nameHandler.getPkFieldName(clazz);
        //更新，主键都是在where
        AutoField pkField = getFields().get(pkFieldName);
        if (pkField != null) {
            getFields().remove(pkFieldName);
            if (!whereBuilder.hasField(pkFieldName)) {
                this.whereBuilder.addCondition(pkField.getName(), pkField.getSqlOperator(), pkField.getFieldOperator(),
                    pkField.getType(), pkField.getValue());
            }
        }

        String tableName = nameHandler.getTableName(clazz, this.whereBuilder.getFields());
        tableName = applyTableAlias(tableName);

        StringBuilder sql = new StringBuilder(COMMAND_OPEN);
        List<Object> params = new ArrayList<Object>();
        sql.append(tableName).append(" SET ");
        for (Map.Entry<String, AutoField> entry : this.autoFields.entrySet()) {
            String columnName = nameHandler.getColumnName(clazz, entry.getKey());
            columnName = applyColumnAlias(columnName);
            AutoField autoField = entry.getValue();
            if (autoField.isNativeField()) {
                String nativeFieldName = tokenParse(autoField.getName(), clazz, nameHandler);
                String nativeValue = tokenParse(String.valueOf(autoField.getValue()), clazz, nameHandler);
                sql.append(nativeFieldName).append(" = ").append(nativeValue).append(",");
            } else if (autoField.getValue() == null) {
                sql.append(columnName).append(" = NULL,");
            } else {
                sql.append(columnName).append(" = ?,");
                params.add(autoField.getValue());
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        whereBuilder.setTableAlias(getTableAlias());
        BoundSql boundSql = whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sql.append(" ").append(boundSql.getSql());
        params.addAll(boundSql.getParameters());
        return new CriteriaBoundSql(sql.toString(), params);
    }
}
