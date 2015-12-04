package com.dexcoder.jdbc.spring;

import com.dexcoder.jdbc.BoundSql;

import java.util.List;

/**
 * sql信息对象
 * <p/>
 * Created by liyd on 6/27/14.
 */
public class SpringJdbcBoundSql implements BoundSql {

    /**
     * 执行的sql
     */
    private String sql;

    /**
     * 参数，对应sql中的?号
     */
    private List<Object> parameters;

    /**
     * 主键名称
     */
    private String pkName;

    /**
     * 构造方法
     */
    public SpringJdbcBoundSql() {

    }

    /**
     * 构造方法
     *
     * @param sql
     * @param pkName
     * @param parameters
     */
    public SpringJdbcBoundSql(String sql, String pkName, List<Object> parameters) {
        this.sql = sql;
        this.pkName = pkName;
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public String getPkName() {
        return pkName;
    }
}
