package com.dexcoder.commons.validation;

/**
 * Created by liyd on 17/1/23.
 */
public class NotNullValidator implements IValidator {

    public boolean validate(Object obj) {
        return obj != null;
    }

    public String validateCode() {
        return "not.null";
    }

    public String validateMsg(Object validateValue, String validateName) {
        return validateName + "不能为空";
    }
}
