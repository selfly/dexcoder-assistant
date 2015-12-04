package com.dexcoder.jdbc;

import java.util.Map;

/**
 * Created by liyd on 2015-12-4.
 */
public interface SqlFactory {

    /**
     * 获取BoundSql
     *
     * @param namespace
     * @param sqlId
     * @param parameters
     * @return
     */
    BoundSql getBoundSql(String namespace, String sqlId, Map<String, Object> parameters);
}
