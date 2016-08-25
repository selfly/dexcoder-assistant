package com.dexcoder.dal.build;

import com.dexcoder.dal.BoundSql;

/**
 * Created by liyd on 2015-12-7.
 */
public class DeleteBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "DELETE FROM ";

    /**
     * whereBuilder
     */
    private SqlBuilder            whereBuilder;

    public DeleteBuilder(Class<?> clazz) {
        super(clazz);
        whereBuilder = new WhereBuilder(clazz);
    }

    public void addField(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                         Object value) {
        this.addCondition(fieldName, logicalOperator, fieldOperator, type, value);
    }

    public void addCondition(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        whereBuilder.addCondition(fieldName, logicalOperator, fieldOperator, type, value);
    }

    public BoundSql buildBoundSql(Object entity, boolean isIgnoreNull) {
        //构建到whereBuilder
        whereBuilder.getMetaTable().mappingHandler(metaTable.getMappingHandler()).tableAlias(metaTable.getTableAlias())
            .entity(entity, isIgnoreNull);
        //这里必须从whereBuilder的MetaTable中获取表名，以便水平分表时能使用正确的表名
        String tableName = whereBuilder.getMetaTable().getTableAndAliasName();
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        sb.append(tableName);
        BoundSql boundSql = whereBuilder.build(entity, isIgnoreNull);
        sb.append(boundSql.getSql());
        return new CriteriaBoundSql(sb.toString(), boundSql.getParameters());
    }
}
