package com.dexcoder.jdbc.batis.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dexcoder.jdbc.batis.build.*;
import com.dexcoder.jdbc.batis.parser.GenericTokenParser;
import com.dexcoder.jdbc.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-11-27.
 */
public class SqlSourceBuilder extends BaseBuilder {

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
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
            Map<String, String> propertiesMap = parseParameterMapping(content);
            String property = propertiesMap.get("property");
            ParameterMapping.Builder builder = new ParameterMapping.Builder(property);
            return builder.build();
        }

        private Map<String, String> parseParameterMapping(String content) {
            try {
                Map<String, String> map = new HashMap<String, String>();
                map.put("property", content);
                return map;

                //                //没有jdbcType等，不需要那么复杂
                //                return new ParameterExpression(content);
            } catch (Exception ex) {
                throw new JdbcAssistantException(
                    "Parsing error was found in mapping #{" + content
                            + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex);
            }
        }
    }

}
