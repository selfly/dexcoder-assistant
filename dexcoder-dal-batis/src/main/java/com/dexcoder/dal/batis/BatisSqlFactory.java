package com.dexcoder.dal.batis;

import java.util.HashMap;
import java.util.Map;

import com.dexcoder.commons.utils.ArrUtils;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.SqlFactory;
import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.build.MappedStatement;

/**
 * Created by liyd on 2015-11-24.
 */
public class BatisSqlFactory implements SqlFactory {

    /** 默认参数名 */
    private static final String DEFAULT_PARAMETERS_KEY = "parameters";

    private Configuration       configuration;

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
        String paramKey = StrUtils.isBlank(expectParamKey) ? DEFAULT_PARAMETERS_KEY : expectParamKey;
        Map<String, Object> map = new HashMap<String, Object>();
        if (parameters.length == 1) {
            map.put(paramKey, parameters[0]);
        } else {
            map.put(paramKey, parameters);
        }
        return map;
    }
}
