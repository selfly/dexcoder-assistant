package com.dexcoder.dal.build;

import com.dexcoder.commons.utils.StrUtils;

/**
 * 组装sql时的列信息
 * <p/>
 * Created by liyd on 7/7/14.
 */
public class AutoField {

    /**
     * native field 正则
     */
    public static final String REGEX_NATIVE_FIELD = "(^[\\[].+[\\]]$)|(^[{].+[}]$)";

    /**
     * 名称
     */
    private String             name;

    /**
     * 别名
     */
    private String             annotationName;

    /**
     * 逻辑操作符 and or
     */
    private String             logicalOperator;

    /**
     * 属性操作符 值大于、小于、in等
     */
    private String             fieldOperator;

    /**
     * 值
     */
    private Object             value;

    /**
     * 类型
     */
    private AutoFieldType      type;

    /**
     * 是否native属性
     * 
     * @return
     */
    public boolean isNativeField() {
        return name.matches(REGEX_NATIVE_FIELD);
    }

    /**
     * 是否括号
     *
     * @return
     */
    public boolean isBracket() {
        return type == AutoFieldType.BRACKET_BEGIN || type == AutoFieldType.BRACKET_END;
    }

    /**
     * fieldOperator是否需要括号
     * 
     * @return
     */
    public boolean isFieldOperatorNeedBracket() {
        return StrUtils.indexOf(StrUtils.upperCase(fieldOperator), "IN") != -1;
    }

    public String getName() {
        return name;
    }

    public String getAnnotationName() {
        return annotationName;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public String getFieldOperator() {
        return fieldOperator;
    }

    public Object getValue() {
        return value;
    }

    public AutoFieldType getType() {
        return type;
    }

    public static class Builder {

        private AutoField autoField = new AutoField();

        public Builder name(String fieldName) {
            autoField.name = fieldName;
            return this;
        }

        public Builder logicalOperator(String logicalOperator) {
            autoField.logicalOperator = logicalOperator;
            return this;
        }

        public Builder fieldOperator(String fieldOperator) {
            autoField.fieldOperator = fieldOperator;
            return this;
        }

        public Builder value(Object value) {
            autoField.value = value;
            return this;
        }

        public Builder type(AutoFieldType type) {
            autoField.type = type;
            return this;
        }

        public Builder annotationName(String annotationName) {
            autoField.annotationName = annotationName;
            return this;
        }

        public AutoField build() {
            assert autoField.name != null || autoField.annotationName != null;
            return autoField;
        }
    }
}
