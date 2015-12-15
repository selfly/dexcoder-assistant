package com.dexcoder.dal.batis.build;

import com.dexcoder.dal.batis.xml.SqlNode;

/**
 * Created by liyd on 2015-11-30.
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode) {
        this(configuration, getSql(rootSqlNode));
    }

    public RawSqlSource(Configuration configuration, String sql) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        sqlSource = sqlSourceParser.parse(sql);
    }

    private static String getSql(SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

    public BatisBoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

}
