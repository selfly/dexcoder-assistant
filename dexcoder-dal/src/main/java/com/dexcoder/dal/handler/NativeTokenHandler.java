package com.dexcoder.dal.handler;

import com.dexcoder.dal.build.MetaTable;

/**
 * 解析[]符号
 * 
 * Created by liyd on 2015-12-8.
 */
public class NativeTokenHandler implements TokenHandler {

    private MetaTable metaTable;

    public NativeTokenHandler(MetaTable metaTable) {
        this.metaTable = metaTable;
    }

    public String handleToken(String content) {
        if (metaTable == null) {
            return content;
        }
        return metaTable.getColumnAndTableAliasName(content);
    }
}
