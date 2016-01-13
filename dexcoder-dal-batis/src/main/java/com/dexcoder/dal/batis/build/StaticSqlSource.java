package com.dexcoder.dal.batis.build;

import java.util.List;

/**
 * Created by liyd on 2015-12-1.
 */
public class StaticSqlSource implements SqlSource {

    private String                 sql;
    private List<ParameterMapping> parameterMappings;

    public StaticSqlSource(String sql) {
        this(sql, null);
    }

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    public BatisBoundSql getBoundSql(Object parameterObject) {
        return new BatisBoundSql(sql, parameterMappings,parameterObject);
    }

}
