package com.dexcoder.dal.handler;

import java.io.Serializable;

import com.dexcoder.commons.utils.NameUtils;

/**
 * Created by liyd on 16/8/25.
 */
public class OracleKeyGenerator implements KeyGenerator {

    public String handlePkFieldName(String pkFieldName, String dialect) {
        return String.format("[%s]", pkFieldName);
    }

    public Serializable generateKeyValue(Class<?> clazz, String dialect) {
        //根据实体名获取主键序列名
        String tableName = NameUtils.getUnderlineName(clazz.getSimpleName());
        return String.format("SEQ_%s.NEXTVAL", tableName);
    }
}
