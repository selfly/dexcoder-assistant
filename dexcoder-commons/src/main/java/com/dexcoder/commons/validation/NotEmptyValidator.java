package com.dexcoder.commons.validation;

import java.util.Collection;

/**
 * Created by liyd on 17/1/23.
 */
public class NotEmptyValidator implements Validator {

    public String validateCode() {
        return "not.empty";
    }

    public String validateMsg(Object value,String validateName) {
        return validateName + "不能为空";
    }

    public boolean validate(Object obj) {
        Collection<?> cts = (Collection<?>) obj;
        return cts != null && !cts.isEmpty();
    }
}
