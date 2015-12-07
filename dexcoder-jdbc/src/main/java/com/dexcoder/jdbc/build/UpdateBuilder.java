package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;
import com.dexcoder.jdbc.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 2015-12-4.
 */
public class UpdateBuilder extends AbstractFieldBuilder {

    private static final String COMMAND_OPEN = "UPDATE ";

    private FieldBuilder whereBuilder;

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
        super.mergeEntityFields(entity, AutoFieldType.UPDATE, nameHandler);
        if (StrUtils.isNotBlank(this.pkFieldName)) {
            //更新，主键都是在where
            AutoField pkField = getFields().get(this.pkFieldName);
            getFields().remove(this.pkFieldName);
            this.whereBuilder.addCondition(pkField.getName(), pkField.getSqlOperator(), pkField.getFieldOperator(), pkField.getType(), pkField.getValue());
        }
        String tableName = nameHandler.getTableName(clazz, this.whereBuilder.getFields());

        StringBuilder sql = new StringBuilder(COMMAND_OPEN);
        List<Object> params = new ArrayList<Object>();
        sql.append(tableName);
        for (Map.Entry<String, AutoField> entry : this.autoFields.entrySet()) {
            String columnName = nameHandler.getColumnName(entry.getKey());
            AutoField autoField = entry.getValue();
            if (autoField.getValue() == null) {
                if (!isIgnoreNull) {
                    sql.append(" SET ").append(columnName).append(" = NULL,");
                }
            } else {
                //待添加解析器
                sql.append(" SET ").append(columnName).append(" = ?,");
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
