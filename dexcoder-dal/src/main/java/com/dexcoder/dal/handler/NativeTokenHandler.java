package com.dexcoder.dal.handler;

/**
 * Created by liyd on 2015-12-8.
 */
public class NativeTokenHandler implements TokenHandler {

    private Class<?>    clazz;
    private NameHandler nameHandler;

    public NativeTokenHandler(Class<?> clazz, NameHandler nameHandler) {
        this.clazz = clazz;
        this.nameHandler = nameHandler;
    }

    public String handleToken(String content) {
        return nameHandler.getColumnName(this.clazz, content);
    }
}
