package com.dexcoder.jdbc.batis;

import java.util.List;
import java.util.Map;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.SqlFactory;
import com.dexcoder.jdbc.batis.build.BatisBoundSql;
import com.dexcoder.jdbc.batis.build.Configuration;
import com.dexcoder.jdbc.batis.build.MappedStatement;
import com.dexcoder.jdbc.batis.build.ParameterMapping;

/**
 * Created by liyd on 2015-11-24.
 */
public class BatisSqlFactory implements SqlFactory {

    private Configuration configuration;

    public BatisSqlFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public BoundSql getBoundSql(String namespace, String sqlId, Map<String, Object> parameters) {

        MappedStatement mappedStatement = this.configuration.getMappedStatements().get(namespace + "." + sqlId);
        BatisBoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(parameters);
        String sql = boundSql.getSql();
        System.out.println(sql);

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (ParameterMapping parameterMapping : parameterMappings) {

            String property = parameterMapping.getProperty();

            Object value = boundSql.getAdditionalParameter(property);

            System.out.println(value);
        }

        return null;
    }
}
