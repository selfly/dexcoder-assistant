package com.dexcoder.dal.batis.build;

import java.util.ArrayList;
import java.util.List;

import com.dexcoder.dal.handler.GenericTokenParser;
import com.dexcoder.dal.handler.TokenHandler;

/**
 * Created by liyd on 2015-11-27.
 */
public class SqlSourceBuilder extends BaseBuilder {

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(sql, handler.getParameterMappings());
    }

    private static class ParameterMappingTokenHandler implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        private ParameterMapping buildParameterMapping(String content) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(content);
            return builder.build();
        }
    }

}
