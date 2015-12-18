package com.dexcoder.dal.handler;

import com.dexcoder.commons.utils.StrUtils;

/**
 * Created by liyd on 2015-12-8.
 */
public class NativeTokenHandler implements TokenHandler {

    private Class<?>    clazz;
    private String      alias;
    private NameHandler nameHandler;

    public NativeTokenHandler(Class<?> clazz, String alias, NameHandler nameHandler) {
        this.clazz = clazz;
        this.alias = alias;
        this.nameHandler = nameHandler;
    }

    public String handleToken(String content) {
        String columnName = nameHandler.getColumnName(this.clazz, content);
        if (StrUtils.isBlank(alias)) {
            return columnName;
        }
        return new StringBuilder(alias).append(".").append(columnName).toString();
    }
}
