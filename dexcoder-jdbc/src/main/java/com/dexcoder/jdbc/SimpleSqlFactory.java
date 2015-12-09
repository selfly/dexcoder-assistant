package com.dexcoder.jdbc;

/**
 * Created by liyd on 2015-12-9.
 */
public class SimpleSqlFactory implements SqlFactory {

    public BoundSql getBoundSql(String refSql, String expectParamKey, Object[] parameters) {
        return new SimpleBoundSql(refSql, parameters);
    }
}
