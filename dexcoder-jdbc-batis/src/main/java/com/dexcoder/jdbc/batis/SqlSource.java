package com.dexcoder.jdbc.batis;

/**
 * Created by liyd on 2015-11-25.
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
