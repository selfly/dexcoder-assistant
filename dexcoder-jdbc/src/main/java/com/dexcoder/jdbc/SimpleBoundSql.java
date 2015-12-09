package com.dexcoder.jdbc;

/**
 * Created by liyd on 2015-12-9.
 */
public class SimpleBoundSql implements BoundSql {

    private String   sql;

    private Object[] parameters;

    public SimpleBoundSql(String sql, Object[] parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParameters() {
        return this.parameters;
    }
}
