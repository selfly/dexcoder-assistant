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

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = buildAutoField(fieldName, null, fieldOperator, type, null);
        this.autoFields.put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        throw new JdbcAssistantException("OrderByBuilder不支持条件添加");
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        if (getFields().isEmpty()) {
            sb.append(applyColumnAlias(nameHandler.getPkColumnName(clazz))).append(" DESC");
        } else {
            for (Map.Entry<String, AutoField> entry : getFields().entrySet()) {
                String columnName = applyColumnAlias(nameHandler.getColumnName(clazz, entry.getKey()));
                sb.append(columnName).append(" ").append(entry.getValue().getFieldOperator()).append(",");
            }
            if (sb.length() > 10) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return new CriteriaBoundSql(sb.toString(), null);
    }
}
