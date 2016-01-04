package com.dexcoder.dal.handler;

import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.build.AutoTable;

/**
 * 解析[]符号
 * 
 * Created by liyd on 2015-12-8.
 */
public class NativeTokenHandler implements TokenHandler {

    private AutoTable autoTable;

    public NativeTokenHandler(AutoTable autoTable) {
        this.autoTable = autoTable;
    }

    public String handleToken(String content) {
        String columnName = autoTable.getNameHandler().getColumnName(autoTable.getTableClass(), content);
        if (StrUtils.isBlank(autoTable.getTableAlias())) {
            return columnName;
        }
        return new StringBuilder(autoTable.getTableAlias()).append(".").append(columnName).toString();
    }
}
