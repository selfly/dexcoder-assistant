package com.dexcoder.dal.build;

import java.util.HashSet;
import java.util.Set;

import com.dexcoder.dal.handler.GenericTokenParser;
import com.dexcoder.dal.handler.NativeTokenHandler;
import com.dexcoder.dal.handler.TokenHandler;

/**
 * Created by liyd on 2015-12-7.
 */
public abstract class AbstractSqlBuilder implements SqlBuilder {

    /**
     * 符号中内容会被转换(field -> column)
     */
    public static final String[]      NATIVE_FIELD_TOKEN = { "[", "]" };

    /**
     * 表信息
     */
    protected AutoTable.Builder       autoTableBuilder;

    /**
     * parser map
     */
    protected Set<GenericTokenParser> tokenParsers;

    public AbstractSqlBuilder(Class<?> clazz) {
        autoTableBuilder = new AutoTable.Builder(clazz);
    }

    public void setTableAlias(String tableAlias) {
        autoTableBuilder.tableAlias(tableAlias);
    }

    /**
     * 初始化 TokenParsers
     *
     * @param autoTable the auto table
     * @return set set
     */
    protected Set<GenericTokenParser> initTokenParsers(AutoTable autoTable) {
        if (tokenParsers == null) {
            tokenParsers = new HashSet<GenericTokenParser>(1);
            TokenHandler tokenHandler = new NativeTokenHandler(autoTable);
            tokenParsers.add(new GenericTokenParser(NATIVE_FIELD_TOKEN[0], NATIVE_FIELD_TOKEN[1], tokenHandler));
        }
        return tokenParsers;
    }

    /**
     * TokenParsers 解析
     *
     * @param content the content
     * @param autoTable the auto table
     * @return string
     */
    protected String tokenParse(String content, AutoTable autoTable) {
        Set<GenericTokenParser> tokenParsers = initTokenParsers(autoTable);
        String result = content;
        for (GenericTokenParser tokenParser : tokenParsers) {
            result = tokenParser.parse(result);
        }
        return result;
    }

}
