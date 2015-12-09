package com.dexcoder.jdbc.handler;

/**
 * Created by liyd on 2015-12-8.
 */
public class NativeTokenHandler implements TokenHandler {

    private NameHandler nameHandler;

    public NativeTokenHandler(NameHandler nameHandler) {
        this.nameHandler = nameHandler;
    }

    public String handleToken(String content) {
        return nameHandler.getColumnName(content);
    }
}
