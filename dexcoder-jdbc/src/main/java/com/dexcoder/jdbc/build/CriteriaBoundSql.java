package com.dexcoder.jdbc.build;

import java.util.List;

import com.dexcoder.jdbc.BoundSql;

/**
 * Created by liyd on 2015-12-7.
 */
public class CriteriaBoundSql implements BoundSql {

    private String       sql;

    private List<Object> parameters;

    public CriteriaBoundSql(String sql, List<Object> parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    public String getSql() {
        return this.sql;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }
}
