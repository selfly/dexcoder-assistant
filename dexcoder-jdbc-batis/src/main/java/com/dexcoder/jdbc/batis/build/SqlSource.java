package com.dexcoder.jdbc.batis.build;

/**
 * Created by liyd on 2015-11-25.
 */
public interface SqlSource {

    BatisBoundSql getBoundSql(Object parameterObject);

}
