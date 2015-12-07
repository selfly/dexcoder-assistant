package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;
import com.dexcoder.jdbc.exceptions.JdbcAssistantException;
import com.dexcoder.jdbc.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 2015-12-4.
 */
public class WhereBuilder extends AbstractFieldBuilder {

    private static final String COMMAND_OPEN = " WHERE ";

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = buildAutoField(fieldName, null, fieldOperator, AutoFieldType.WHERE, value);
        this.autoFields.put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        this.addField(fieldName, sqlOperator, fieldOperator, type, value);
    }


    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        if (!hasFields()) {
            throw new JdbcAssistantException("where条件不能为空");
        }
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        List<Object> params = new ArrayList<Object>();
        for (Map.Entry<String, AutoField> entry : getFields().entrySet()) {
            String columnName = nameHandler.getColumnName(entry.getKey());
            AutoField autoField = entry.getValue();
            if (autoField.getValue() == null) {
                sb.append(columnName).append(" IS NULL ");
            } else if (autoField.getValue() instanceof Object[]) {
                this.processArrayArgs(sb, params, columnName, autoField);
            } else {
                sb.append(columnName).append(" ").append(autoField.getFieldOperator()).append(" ").append("? ");
                params.add(autoField.getValue());
            }
        }
        return new CriteriaBoundSql(sb.toString(), params);
    }

    /**
     * 处理数组参数
     *
     * @param sb
     * @param params
     * @param columnName
     * @param autoField
     */
    private void processArrayArgs(StringBuilder sb, List<Object> params, String columnName, AutoField autoField) {
        Object[] args = (Object[]) autoField.getValue();
        sb.append(autoField.getSqlOperator()).append(" (");
        if (StrUtils.indexOf(StrUtils.upperCase(autoField.getFieldOperator()), "IN") != -1) {
            sb.append(columnName).append(" ").append(autoField.getFieldOperator()).append(" (");
            for (int i = 0; i < args.length; i++) {
                sb.append("?");
                if (i != args.length - 1) {
                    sb.append(",");
                }
                params.add(args[i]);
            }
            sb.append(") ");
        } else {
            sb.append(" (");
            for (int i = 0; i < args.length; i++) {
                sb.append(columnName).append(" ").append(autoField.getFieldOperator()).append(" ").append("?");
                if (i != args.length - 1) {
                    sb.append(",");
                }
                params.add(args[i]);
            }
            sb.append(") ");
        }
    }
}
