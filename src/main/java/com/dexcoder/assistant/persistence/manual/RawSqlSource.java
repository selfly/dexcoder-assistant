package com.dexcoder.assistant.persistence.manual;

import java.util.HashMap;

/**
 * Created by liyd on 2015-11-30.
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode) {
        this(configuration, getSql(configuration, rootSqlNode));
    }

    public RawSqlSource(Configuration configuration, String sql) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        sqlSource = sqlSourceParser.parse(sql, new HashMap<String, Object>());
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration, null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

}

