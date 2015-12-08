package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.NameHandler;
import com.dexcoder.jdbc.exceptions.JdbcAssistantException;

import java.util.Map;

/**
 * Created by liyd on 2015-12-4.
 */
public class OrderByBuilder extends AbstractSqlBuilder {

    protected static final String COMMAND_OPEN = " ORDER BY ";

    public void addField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = buildAutoField(fieldName, null, fieldOperator, type, null);
        this.autoFields.put(fieldName, autoField);
    }

    public void addCondition(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type, Object value) {
        throw new JdbcAssistantException("OrderByBuilder不支持条件添加");
    }

    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
        StringBuilder sb = new StringBuilder(COMMAND_OPEN);
        if (getFields().isEmpty()) {
            sb.append(nameHandler.getPKName(clazz)).append(" DESC");
        } else {
            for (Map.Entry<String, AutoField> entry : getFields().entrySet()) {
                String columnName = nameHandler.getColumnName(entry.getKey());
                sb.append(columnName).append(" ").append(entry.getValue().getFieldOperator()).append(",");
            }
            if (sb.length() > 10) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return new CriteriaBoundSql(sb.toString(), null);
    }


    //    /**
//     * asc 排序属性
//     *
//     * @param field the field
//     */
//    public void asc(String... field) {
//        for (String f : field) {
//            this.buildAutoField(f, null, "ASC", AutoFieldType.ORDER_BY, null);
//        }
//    }
//
//    /**
//     * desc 排序属性
//     *
//     * @param field the field
//     */
//    public void desc(String... field) {
//        for (String f : field) {
//            this.buildAutoField(f, null, "DESC", AutoFieldType.ORDER_BY, null);
//        }
//    }

}
