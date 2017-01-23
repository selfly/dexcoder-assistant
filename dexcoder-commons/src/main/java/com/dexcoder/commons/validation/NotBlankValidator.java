package com.dexcoder.commons.validation;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 17/1/23.
 */
public class NotBlankValidator implements IValidator {

    public boolean validate(Object obj) {
        return StringUtils.isNotBlank((String) obj);
    }

    public String validateCode() {
        return "not.blank";
    }

    public String validateMsg(Object validateValue, String validateName) {
        return validateName + "不能为空";
    }
}
