package com.dexcoder.jdbc.parser;

import com.dexcoder.jdbc.NameHandler;

/**
 * Created by liyd on 2015-12-8.
 */
public class FieldTokenHandler implements TokenHandler {

    private NameHandler nameHandler;

    public FieldTokenHandler(NameHandler nameHandler) {
        this.nameHandler = nameHandler;
    }

    public String handleToken(String content) {
        return nameHandler.getColumnName(content);
    }
}
