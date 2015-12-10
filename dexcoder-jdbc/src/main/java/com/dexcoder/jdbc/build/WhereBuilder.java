package com.dexcoder.jdbc.build;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.exceptions.JdbcAssistantException;
import com.dexcoder.jdbc.handler.NameHandler;

/**
 * Created by liyd on 2015-12-4.
 */
public class WhereBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = " WHERE ";

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = buildAutoField(fieldName, sqlOperator, fieldOperator, type, value);
        this.autoFields.put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                             Object value) {
        Object obj = value;
        while (obj instanceof Object[] && Array.getLength(obj) == 1) {
            obj = ((Object[]) obj)[0];
        }
        this.addField(fieldName, sqlOperator, fieldOperator, type, obj);
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        if (!hasFields()) {
            throw new JdbcAssistantException("where条件不能为空");
        }
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        List<Object> params = new ArrayList<Object>();
        AutoField preAutoFile = null;
        for (Map.Entry<String, AutoField> entry : getFields().entrySet()) {
            String columnName = nameHandler.getColumnName(clazz, entry.getKey());
            AutoField autoField = entry.getValue();
            if (StrUtils.isNotBlank(autoField.getSqlOperator()) && sb.length() > COMMAND_OPEN.length()
                && !isFieldBracketBegin(preAutoFile)) {
                sb.append(autoField.getSqlOperator()).append(" ");
            }
            if (autoField.isNativeField()) {
                String nativeFieldName = tokenParse(autoField.getName(), clazz, nameHandler);
                String nativeValue = tokenParse(String.valueOf(autoField.getValue()), clazz, nameHandler);
                sb.append(nativeFieldName).append(" ").append(autoField.getFieldOperator()).append(" ")
                    .append(nativeValue).append(" ");
            } else if (autoField.getType() == AutoFieldType.BRACKET_BEGIN
                       || autoField.getType() == AutoFieldType.BRACKET_END) {
                sb.append(autoField.getName()).append(" ");
            } else if (autoField.getValue() == null) {
                sb.append(columnName).append(" IS NULL ");
            } else if (autoField.getValue() instanceof Object[]) {
                this.processArrayArgs(sb, params, columnName, autoField, preAutoFile);
            } else {
                sb.append(columnName).append(" ").append(autoField.getFieldOperator()).append(" ").append(" ? ");
                params.add(autoField.getValue());
            }
            preAutoFile = autoField;
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
    protected void processArrayArgs(StringBuilder sb, List<Object> params, String columnName, AutoField autoField,
                                    AutoField preAutoField) {
        Object[] args = (Object[]) autoField.getValue();
        if (StrUtils.isNotBlank(autoField.getSqlOperator()) && !isFieldBracketBegin(preAutoField)) {
            sb.append(autoField.getSqlOperator());
        }
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
                    sb.append(" OR ");
                }
                params.add(args[i]);
            }
            sb.append(") ");
        }
    }

    /**
     * 是否括号类型开始
     *
     * @param autoField
     * @return
     */
    protected boolean isFieldBracketBegin(AutoField autoField) {
        return (autoField != null && autoField.getType() == AutoFieldType.BRACKET_BEGIN);
    }
}
