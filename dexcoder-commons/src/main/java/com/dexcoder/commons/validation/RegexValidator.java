package com.dexcoder.commons.validation;

/**
 * Created by liyd on 17/1/24.
 */
public class RegexValidator implements Validator {

    public boolean validate(Object value) {
        String[] values = (String[]) value;
        return values[0].matches(values[1]);
    }

    public String validateCode() {
        return "regex.error";
    }

    public String validateMsg(Object value, String validateName) {
        return validateName + "格式不正确";
    }
}
