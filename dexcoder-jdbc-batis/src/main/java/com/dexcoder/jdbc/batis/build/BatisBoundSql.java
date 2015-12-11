package com.dexcoder.jdbc.batis.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.batis.reflection.MetaObject;

/**
 * Created by liyd on 2015-11-25.
 */
public class BatisBoundSql implements BoundSql {

    private String                 sql;
    private List<ParameterMapping> parameterMappings;
//    private Object                 parameterObject;
    private Map<String, Object>    additionalParameters;
    private MetaObject             metaParameters;

    public BatisBoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings,
                         Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
//        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<String, Object>();
        this.metaParameters = configuration.newMetaObject(additionalParameters);
    }

    public List<Object> getParameters() {
        List<Object> params = new ArrayList<Object>();
        if (parameterMappings == null) {
            return params;
        }
        for (ParameterMapping parameterMapping : parameterMappings) {
            String property = parameterMapping.getProperty();
            Object value = getAdditionalParameter(property);
            params.add(value);
        }
        return params;
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setAdditionalParameter(String name, Object value) {
        metaParameters.setValue(name, value);
    }

    public Object getAdditionalParameter(String name) {
        return metaParameters.getValue(name);
    }
}
