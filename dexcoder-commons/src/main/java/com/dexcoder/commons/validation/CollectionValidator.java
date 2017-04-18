package com.dexcoder.commons.validation;

import java.util.Collection;

/**
 * Created by liyd on 17/3/12.
 */
public class CollectionValidator implements Validator {

    public static final int MUST_EMPTY = 0;
    public static final int MIN_SIZE   = 1;
    public static final int MAX_SIZE   = 2;
    public static final int EQ_SIZE    = 3;

    private int             type;

    public CollectionValidator(int type) {
        this.type = type;
    }

    public boolean validate(Object value) {

        if (type == MUST_EMPTY) {
            return value == null || ((Collection) value).isEmpty();
        } else if (type == MIN_SIZE) {
            Object[] values = (Object[]) value;
            Collection collection = (Collection) values[0];
            return collection != null && collection.size() >= (Integer) values[1];
        } else if (type == MAX_SIZE) {
            Object[] values = (Object[]) value;
            Collection collection = (Collection) values[0];
            return collection == null || collection.size() <= (Integer) values[1];
        } else if (type == EQ_SIZE) {
            Object[] values = (Object[]) value;
            Collection collection = (Collection) values[0];
            return collection != null && collection.size() == ((Integer) values[1]);
        } else {

            throw new UnsupportedOperationException("不支持的集合操作");
        }
    }

    public String validateCode() {
        if (type == MUST_EMPTY) {
            return "must.empty";
        } else if (type == MIN_SIZE) {
            return "min.size";
        } else if (type == MAX_SIZE) {
            return "max.size";
        } else if (type == EQ_SIZE) {
            return "eq.size";
        } else {
            return "";
        }
    }

    public String validateMsg(Object value, String validateName) {
        if (type == MUST_EMPTY) {
            return validateName + "必须为空";
        } else if (type == MIN_SIZE) {
            Object[] values = (Object[]) value;
            return validateName + "允许最小大小为" + values[1];
        } else if (type == MAX_SIZE) {
            Object[] values = (Object[]) value;
            return validateName + "允许最大大小为" + values[1];
        } else if (type == EQ_SIZE) {
            Object[] values = (Object[]) value;
            return validateName + "大小必须为" + values[1];
        } else {
            return "";
        }
    }
}
