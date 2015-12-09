package com.dexcoder.jdbc.batis;

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

    /**
     * Not used
     *
     * @return
     */
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParameterMapping{");
        //sb.append("configuration=").append(configuration); // configuration doesn't have a useful .toString()
        sb.append("property='").append(property).append('\'');
        sb.append(", expression='").append(expression).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
