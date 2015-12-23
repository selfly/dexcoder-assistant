package com.dexcoder.dal.build;

import java.util.List;

import com.dexcoder.dal.BoundSql;

/**
 * Created by liyd on 2015-12-7.
 */
public class CriteriaBoundSql implements BoundSql {

    /**
     * sql
     */
    private String       sql;

    /**
     * parameters
     */
    private List<Object> parameters;

    /**
     * Constructor
     *
     * @param sql
     * @param parameters
     */
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
