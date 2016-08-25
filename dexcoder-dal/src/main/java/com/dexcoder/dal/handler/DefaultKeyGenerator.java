package com.dexcoder.dal.handler;

import java.io.Serializable;

import com.dexcoder.commons.utils.UUIDUtils;

/**
 * Created by liyd on 16/8/25.
 */
public class DefaultKeyGenerator implements KeyGenerator {

    public String handlePkFieldName(String pkFieldName, String dialect) {
        return pkFieldName;
    }

    public Serializable generateKeyValue(Class<?> clazz, String dialect) {
        return UUIDUtils.getUUID32();
    }
}
