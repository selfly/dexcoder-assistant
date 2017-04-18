package com.dexcoder.commons.validation;

import java.util.Collection;

/**
 * Created by liyd on 17/1/23.
 */
public class NotEmptyValidator implements Validator {

    public String validateCode() {
        return "not.empty";
    }

    public String validateMsg(Object value, String validateName) {
        return validateName + "不能为空";
    }

    public boolean validate(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Collection) {
            Collection<?> cts = (Collection<?>) obj;
            return cts != null && !cts.isEmpty();
        } else if (obj.getClass().isArray()) {
            return ((Object[]) obj).length > 0;
        } else if (obj instanceof String) {
            return ((String) obj).isEmpty();
        } else {
            throw new UnsupportedOperationException("不支持的参数类型");
        }

    }
}
