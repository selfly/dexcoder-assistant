package com.dexcoder.dal.build;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.handler.NameHandler;

/**
 * Created by liyd on 2015-12-7.
 */
public class DeleteBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "DELETE FROM ";

    /**
     * whereBuilder
     */
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
        metaTable = new MetaTable.Builder(metaTable).nameHandler(nameHandler).build();
        //构建到whereBuilder
        new MetaTable.Builder(whereBuilder.getMetaTable()).tableClass(clazz).tableAlias(metaTable.getTableAlias())
            .entity(entity, isIgnoreNull).nameHandler(nameHandler).build();
        //这里必须从whereBuilder的MetaTable中获取表名，以便水平分表时能使用正确的表名
        String tableName = whereBuilder.getMetaTable().getTableAndAliasName();
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        sb.append(tableName).append(" ");
        BoundSql boundSql = whereBuilder.build(clazz, entity, isIgnoreNull, nameHandler);
        sb.append(boundSql.getSql());
        return new CriteriaBoundSql(sb.toString(), boundSql.getParameters());
    }
}
