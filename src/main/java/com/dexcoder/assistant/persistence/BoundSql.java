package com.dexcoder.assistant.persistence;

import java.util.List;

/**
 * sql信息对象
 * 
 * Created by liyd on 6/27/14.
 */
public class BoundSql {

    /** 执行的sql */
    private String       sql;

    /** 参数，对应sql中的?号 */
    private List<Object> params;

    /** 主键名称 */
    private String       primaryKey;

    /**
     * 构造方法
     */
    public BoundSql() {

    }

    /**
     * 构造方法
     * 
     * @param sql
     * @param primaryKey
     * @param params
     */
    public BoundSql(String sql, String primaryKey, List<Object> params) {
        this.sql = sql;
        this.primaryKey = primaryKey;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
