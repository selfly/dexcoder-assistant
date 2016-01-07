package com.dexcoder.dal.build;

import java.util.Map;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.exceptions.JdbcAssistantException;
import com.dexcoder.dal.handler.NameHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public class OrderByBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = " ORDER BY ";

    public void addField(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                         Object value) {
        AutoField autoField = new AutoField.Builder().name(fieldName).fieldOperator(fieldOperator).type(type).build();
        metaTable.getAutoFields().put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String logicalOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        throw new JdbcAssistantException("OrderByBuilder不支持设置条件");
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        metaTable = new MetaTable.Builder(metaTable).tableClass(clazz).nameHandler(nameHandler).build();
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        if (metaTable.getAutoFields().isEmpty()) {
            sb.append(metaTable.getColumnAndTableAliasName(metaTable.getPkFieldName())).append(" DESC");
        } else {
            for (Map.Entry<String, AutoField> entry : metaTable.getAutoFields().entrySet()) {
                String columnName = metaTable.getColumnAndTableAliasName(entry.getKey());
                sb.append(columnName).append(" ").append(entry.getValue().getFieldOperator()).append(",");
            }
            if (sb.length() > 10) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return new CriteriaBoundSql(sb.toString(), null);
    }
}
