package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;
import com.dexcoder.jdbc.parser.GenericTokenParser;
import com.dexcoder.jdbc.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 2015-12-4.
 */
public class UpdateBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "UPDATE ";

    private SqlBuilder whereBuilder;

    public UpdateBuilder() {
        whereBuilder = new WhereBuilder();
    }

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = this.buildAutoField(fieldName, sqlOperator, fieldOperator, type, value);
        this.autoFields.put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        whereBuilder.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        super.mergeEntityFields(entity, AutoFieldType.UPDATE, nameHandler, isIgnoreNull);
        if (StrUtils.isNotBlank(this.pkFieldName)) {
            //更新，主键都是在where
            AutoField pkField = getFields().get(this.pkFieldName);
            getFields().remove(this.pkFieldName);
            if (!whereBuilder.hasField(this.pkFieldName)) {
                this.whereBuilder.addCondition(pkField.getName(), pkField.getSqlOperator(), pkField.getFieldOperator(), pkField.getType(), pkField.getValue());
            }
        }
        String tableName = nameHandler.getTableName(clazz, this.whereBuilder.getFields());

        StringBuilder sql = new StringBuilder(COMMAND_OPEN);
        List<Object> params = new ArrayList<Object>();
        sql.append(tableName).append(" SET ");
        for (Map.Entry<String, AutoField> entry : this.autoFields.entrySet()) {
            String columnName = nameHandler.getColumnName(entry.getKey());
            AutoField autoField = entry.getValue();
            if (autoField.isNativeField()) {
                GenericTokenParser tokenParser = super.getTokenParser(AutoField.NATIVE_OPEN, AutoField.NATIVE_CLOSE, nameHandler);
                String nativeFieldName = tokenParser.parse(autoField.getName());
                String nativeValue = tokenParser.parse(String.valueOf(autoField.getValue()));
                sql.append(nativeFieldName).append(" = ").append(nativeValue).append(",");
            } else if (autoField.getValue() == null) {
                sql.append(columnName).append(" = NULL,");
            } else {
                sql.append(columnName).append(" = ?,");
                params.add(autoField.getValue());
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        BoundSql boundSql = this.whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sql.append(" ").append(boundSql.getSql());
        params.addAll(boundSql.getParameters());
        return new CriteriaBoundSql(sql.toString(), params);
    }
}
