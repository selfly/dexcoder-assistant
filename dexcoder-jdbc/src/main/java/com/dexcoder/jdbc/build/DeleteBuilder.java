package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;

/**
 * Created by liyd on 2015-12-7.
 */
public class DeleteBuilder extends AbstractFieldBuilder {

    private static final String COMMAND_OPEN = "DELETE FROM ";

    private FieldBuilder whereBuilder;

    public DeleteBuilder() {
        whereBuilder = new WhereBuilder();
    }

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        this.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        whereBuilder.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        super.mergeEntityFields(entity, AutoFieldType.WHERE, nameHandler);
        whereBuilder.getFields().putAll(this.getFields());
        String tableName = nameHandler.getTableName(clazz, whereBuilder.getFields());
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        sb.append(tableName).append(" ");
        BoundSql boundSql = whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sb.append(boundSql.getSql());
        return new CriteriaBoundSql(sb.toString(), boundSql.getParameters());
    }
}
