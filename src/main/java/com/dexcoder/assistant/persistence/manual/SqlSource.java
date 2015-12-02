package com.dexcoder.assistant.persistence.manual;

/**
 * Created by liyd on 2015-11-25.
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
