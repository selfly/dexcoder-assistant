package com.dexcoder.commons.validation;

/**
 * Created by liyd on 17/3/19.
 */
public class BooleanValidator implements Validator {

    public static final int FALSE = 0;

    public static final int TRUE  = 1;

    private int             type;

    public BooleanValidator(int type) {
        this.type = type;
    }

    public boolean validate(Object value) {
        if (value == null) {
            return false;
        }
        if (this.type == FALSE) {
            return !((Boolean) value);
        } else if (this.type == TRUE) {
            return ((Boolean) value);
        } else {
            return false;
        }
    }

    public String validateCode() {
        if (this.type == FALSE) {
            return "not.eq.false";
        } else {
            return "not.eq.true";
        }
    }

    public String validateMsg(Object value, String validateName) {
        if (this.type == FALSE) {
            return validateName + "必须为false";
        } else if (this.type == TRUE) {
            return validateName + "必须为true";
        } else {
            return "";
        }
    }
}
