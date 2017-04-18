package com.dexcoder.commons.validation;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 17/2/13.
 */
public class StringValidator implements Validator {

    public static final int NOT_BLANK      = 0;

    public static final int NOT_EMPTY      = 1;

    public static final int EQ             = 2;

    public static final int EQ_IGNORE_CASE = 3;

    public static final int MIN_LENGTH     = 4;

    public static final int MAX_LENGTH     = 5;

    public static final int EQ_LENGTH      = 6;

    public static final int NOT_EQ         = 7;

    private int             type;

    public StringValidator(int type) {
        this.type = type;
    }

    public boolean validate(Object value) {

        if (type == NOT_BLANK) {
            return StringUtils.isNotBlank((String) value);
        } else if (type == NOT_EMPTY) {
            return StringUtils.isNotEmpty((String) value);
        } else if (type == EQ) {
            Object[] values = (Object[]) value;
            return StringUtils.equals((String) values[0], (String) values[1]);
        } else if (type == EQ_IGNORE_CASE) {
            Object[] values = (Object[]) value;
            return StringUtils.equalsIgnoreCase((String) values[0], (String) values[1]);
        } else if (type == MIN_LENGTH) {
            Object[] values = (Object[]) value;
            return StringUtils.length((String) values[0]) >= (Integer) values[1];
        } else if (type == MAX_LENGTH) {
            Object[] values = (Object[]) value;
            return StringUtils.length((String) values[0]) <= (Integer) values[1];
        } else if (type == EQ_LENGTH) {
            Object[] values = (Object[]) value;
            return StringUtils.length((String) values[0]) == (Integer) values[1];
        } else if (type == NOT_EQ) {
            Object[] values = (Object[]) value;
            return !StringUtils.equals((String) values[0], (String) values[1]);
        } else {
            throw new UnsupportedOperationException("不支持的字符串操作");
        }
    }

    public String validateCode() {
        if (type == NOT_BLANK) {
            return "not.blank";
        } else if (type == NOT_EMPTY) {
            return "not.empty";
        } else if (type == EQ) {
            return "must.eq";
        } else if (type == EQ_IGNORE_CASE) {
            return "must.eq.ignore.case";
        } else if (type == MIN_LENGTH) {
            return "min.length";
        } else if (type == MAX_LENGTH) {
            return "max.length";
        } else if (type == NOT_EQ) {
            return "not.eq";
        } else {
            return "";
        }
    }

    public String validateMsg(Object value, String validateName) {
        if (type == NOT_BLANK) {
            return validateName + "不能为空";
        } else if (type == NOT_EMPTY) {
            return validateName + "不能为空";
        } else if (type == EQ) {
            return validateName + "不相同";
        } else if (type == EQ_IGNORE_CASE) {
            return validateName + "不相同";
        } else if (type == MIN_LENGTH) {
            Object[] values = (Object[]) value;
            return validateName + "允许最小长度为" + values[1];
        } else if (type == MAX_LENGTH) {
            Object[] values = (Object[]) value;
            return validateName + "允许最大长度为" + values[1];
        } else if (type == NOT_EQ) {
            Object[] values = (Object[]) value;
            return validateName + "不能等于" + values[1];
        } else {
            return "";
        }
    }
}
