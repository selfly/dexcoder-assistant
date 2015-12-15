package com.dexcoder.dal;

import java.util.Arrays;

import com.dexcoder.dal.build.CriteriaBoundSql;

/**
 * Created by liyd on 2015-12-9.
 */
public class SimpleSqlFactory implements SqlFactory {

    public BoundSql getBoundSql(String refSql, String expectParamKey, Object[] parameters) {
        return new CriteriaBoundSql(refSql, parameters == null ? null : Arrays.asList(parameters));
    }
}
