package com.dexcoder.assistant.persistence.manual;

import java.util.Map;

/**
 * Created by liyd on 2015-11-24.
 */
public class SqlFactory {

    private Configuration configuration;

    public SqlFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public void getBoundSql(String namespace, String sqlId, Map<String, Object> parameters) {

        MappedStatement mappedStatement = this.configuration.getMappedStatements().get(namespace + sqlId);
        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(parameters);
        String sql = boundSql.getSql();
        System.out.println(sql);
    }
}
