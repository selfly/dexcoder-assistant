package com.dexcoder.jdbc.build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyd on 2015-12-4.
 */
public abstract class FieldBuilder {

    /**
     * 操作的字段
     */
    protected List<AutoField> autoFields;

    /**
     * 是否设置了白名单
     */
    protected boolean isInclude = false;

    /**
     * 是否设置了黑名单
     */
    protected boolean isExclude = false;

    protected FieldBuilder() {
        autoFields = new ArrayList<AutoField>();
    }

    /**
     * 构建操作的字段
     *
     * @param fieldName     the field name
     * @param sqlOperator   the build operator
     * @param fieldOperator the field operator
     * @param type          the type
     * @param value         the values
     * @return auto field
     */
    protected void buildAutoField(String fieldName, String sqlOperator,
                                  String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = new AutoField();
        autoField.setName(fieldName);
        autoField.setSqlOperator(sqlOperator);
        autoField.setFieldOperator(fieldOperator);
        autoField.setValue(value);
        autoField.setType(type);
        this.autoFields.add(autoField);
    }

//    /**
//     * 构建只有主键的fieldMap
//     *
//     * @param fieldName
//     * @param value
//     * @return
//     */
//    @SuppressWarnings("serial")
//    public  Map<String, AutoField> buildPkFieldMap(String fieldName, Object value) {
//        final AutoField autoField = buildAutoField(fieldName, null, null, AutoFieldType.WHERE,
//                value);
//        return new LinkedHashMap<String, AutoField>() {
//            {
//                put(autoField.getName(), autoField);
//            }
//        };
//    }


}
