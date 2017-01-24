package com.dexcoder.commons.validation;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 17/1/23.
 */
public class NotBlankValidator implements Validator {

    public boolean validate(Object obj) {
        return StringUtils.isNotBlank((String) obj);
    }

    public String validateCode() {
        return "not.blank";
    }

    public String validateMsg(Object value, String validateName) {
        return validateName + "不能为空";
    }
}
