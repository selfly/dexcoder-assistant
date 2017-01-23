package com.dexcoder.commons.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * 校验器工厂，保存校验器实例以重用，避免每次太多的new
 * 
 * Created by liyd on 17/1/23.
 */
public class ValidatorFactory {

    private static final Map<String, IValidator> VALIDATOR_MAP = new HashMap<String, IValidator>();

    public static final String                   NOT_BLANK     = "NOT_BLANK";

    static {

        VALIDATOR_MAP.put(NOT_BLANK, new NotBlankValidator());

    }

    public static IValidator getValidator(String special) {
        return null;
    }

}
