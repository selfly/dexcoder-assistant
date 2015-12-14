package com.dexcoder.jdbc.batis;

import java.util.HashMap;
import java.util.Map;

import com.dexcoder.commons.utils.ArrUtils;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.SqlFactory;
import com.dexcoder.jdbc.batis.build.Configuration;
import com.dexcoder.jdbc.batis.build.MappedStatement;

/**
 * Created by liyd on 2015-11-24.
 */
public class BatisSqlFactory implements SqlFactory {

    private Configuration configuration;

    public BatisSqlFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public BoundSql getBoundSql(String refSql, String expectParamKey, Object[] parameters) {

        Map<String, Object> params = this.processParameters(expectParamKey, parameters);
        MappedStatement mappedStatement = this.configuration.getMappedStatements().get(refSql);
        return mappedStatement.getSqlSource().getBoundSql(params);
    }

    /**
     * 处理转换参数
     * 
     * @param expectParamKey
     * @param parameters
     * @return
     */
    private Map<String, Object> processParameters(String expectParamKey, Object[] parameters) {

        if (ArrUtils.isEmpty(parameters)) {
            return null;
        }
        String paramKey = StrUtils.isBlank(expectParamKey) ? "item" : expectParamKey;
        Map<String, Object> map = new HashMap<String, Object>();
        if (parameters.length == 1) {
            map.put(paramKey, parameters[0]);
        } else {
            map.put(paramKey, parameters);
        }
        return map;
    }
}
