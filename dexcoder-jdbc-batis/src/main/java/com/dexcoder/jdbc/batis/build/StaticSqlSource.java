package com.dexcoder.jdbc.batis.build;

import java.util.List;

/**
 * Created by liyd on 2015-12-1.
 */
public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    public BatisBoundSql getBoundSql(Object parameterObject) {
        return new BatisBoundSql(configuration, sql, parameterMappings, parameterObject);
    }

}
