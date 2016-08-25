package com.dexcoder.dal.batis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.dexcoder.dal.BoundSql;
import com.dexcoder.dal.SqlFactory;
import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.build.MappedStatement;
import com.dexcoder.dal.exceptions.JdbcAssistantException;

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
        if (mappedStatement == null) {
            throw new JdbcAssistantException("自定义sql没有找到,refSql=" + refSql);
        }
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

        if (ArrayUtils.isEmpty(parameters)) {
            return null;
        }
        String paramKey = StringUtils.isBlank(expectParamKey) ? DEFAULT_PARAMETERS_KEY : expectParamKey;
        Map<String, Object> map = new HashMap<String, Object>();
        if (parameters.length == 1) {
            map.put(paramKey, parameters[0]);
        } else {
            map.put(paramKey, parameters);
        }
        return map;
    }
}
