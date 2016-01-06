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
    public static final String[]      NATIVE_TOKENS = { "{", "}", "[", "]" };

    /**
     * 表信息
     */
    protected MetaTable               metaTable;

    /**
     * parser map
     */
    protected Set<GenericTokenParser> tokenParsers;

    public AbstractSqlBuilder() {
        metaTable = new MetaTable.Builder().initAutoFields().build();
    }

    public MetaTable getMetaTable() {
        return metaTable;
    }

    /**
     * 初始化 TokenParsers
     *
     * @param autoTable the auto table
     * @return set set
     */
    protected Set<GenericTokenParser> initTokenParsers(MetaTable autoTable) {
        if (tokenParsers == null) {
            tokenParsers = new HashSet<GenericTokenParser>(2);
            TokenHandler tokenHandler = new NativeTokenHandler(null);
            tokenParsers.add(new GenericTokenParser(NATIVE_TOKENS[0], NATIVE_TOKENS[1], tokenHandler));
            tokenHandler = new NativeTokenHandler(autoTable);
            tokenParsers.add(new GenericTokenParser(NATIVE_TOKENS[2], NATIVE_TOKENS[3], tokenHandler));
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
    protected String tokenParse(String content, MetaTable autoTable) {
        Set<GenericTokenParser> tokenParsers = initTokenParsers(autoTable);
        String result = content;
        for (GenericTokenParser tokenParser : tokenParsers) {
            result = tokenParser.parse(result);
        }
        return result;
    }

}
