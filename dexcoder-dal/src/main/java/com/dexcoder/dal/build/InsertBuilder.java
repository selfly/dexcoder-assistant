package com.dexcoder.dal.build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.exceptions.JdbcAssistantException;
import com.dexcoder.dal.handler.NameHandler;

/**
 * Created by liyd on 2015-12-7.
 */
public class InsertBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = "INSERT INTO ";

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = AutoField.Builder.build(fieldName, sqlOperator, fieldOperator, type, value, null);
        metaTable.getAutoFields().put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        throw new JdbcAssistantException("InsertBuilder不支持设置条件");
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        metaTable = new MetaTable.Builder(metaTable).tableClass(clazz).entity(entity, isIgnoreNull)
            .nameHandler(nameHandler).build();
        StringBuilder sql = new StringBuilder(COMMAND_OPEN);
        StringBuilder args = new StringBuilder("(");
        List<Object> params = new ArrayList<Object>();
        sql.append(metaTable.getTableName()).append(" (");

        for (Map.Entry<String, AutoField> entry : metaTable.getAutoFields().entrySet()) {
            AutoField autoField = entry.getValue();
            //忽略null值
            if (autoField.getValue() == null && isIgnoreNull) {
                continue;
            }
            //原生类型
            if (autoField.isNativeField()) {
                String nativeFieldName = tokenParse(autoField.getName(), metaTable);
                String nativeValue = tokenParse(String.valueOf(autoField.getValue()), metaTable);
                sql.append(nativeFieldName).append(",");
                args.append(nativeValue);
            } else {
                String columnName = nameHandler.getColumnName(clazz, entry.getKey());
                sql.append(columnName).append(",");
                args.append("?");
            }
            args.append(",");
            params.add(autoField.getValue());
        }
        sql.deleteCharAt(sql.length() - 1);
        args.deleteCharAt(args.length() - 1);
        sql.append(")").append(" VALUES ").append(args.append(")"));
        return new CriteriaBoundSql(sql.toString(), params);
    }
}
