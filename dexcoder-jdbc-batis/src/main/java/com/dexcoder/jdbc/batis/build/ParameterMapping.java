package com.dexcoder.jdbc.batis.build;

/**
 * Created by liyd on 2015-11-27.
 */
public class ParameterMapping {

    private String property;
    private String expression;

    private ParameterMapping() {
    }

    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(String property) {
            parameterMapping.property = property;
        }

        public Builder expression(String expression) {
            parameterMapping.expression = expression;
            return this;
        }

        public ParameterMapping build() {
            return parameterMapping;
        }
    }

    public String getProperty() {
        return property;
    }

}
