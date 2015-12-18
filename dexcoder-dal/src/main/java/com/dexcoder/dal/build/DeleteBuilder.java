package com.dexcoder.dal.build;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.NameHandler;

/**
 * Created by liyd on 2015-12-7.
 */
public class DeleteBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "DELETE FROM ";

    private SqlBuilder            whereBuilder;

    public DeleteBuilder() {
        whereBuilder = new WhereBuilder();
    }

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        this.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        whereBuilder.addCondition(fieldName, sqlOperator, fieldOperator, type, value);
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        super.mergeEntityFields(entity, AutoFieldType.WHERE, nameHandler, isIgnoreNull);
        whereBuilder.setTableAlias(getTableAlias());
        whereBuilder.getFields().putAll(this.getFields());
        String tableName = nameHandler.getTableName(clazz, whereBuilder.getFields());
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        sb.append(applyTableAlias(tableName)).append(" ");
        BoundSql boundSql = whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sb.append(boundSql.getSql());
        return new CriteriaBoundSql(sb.toString(), boundSql.getParameters());
    }
}
