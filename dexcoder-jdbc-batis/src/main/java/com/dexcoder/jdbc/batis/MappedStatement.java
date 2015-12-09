package com.dexcoder.jdbc.batis;

/**
 * Created by liyd on 2015-11-26.
 */
public final class MappedStatement {

    private String resource;
    private Configuration configuration;
    private String id;
    private SqlSource sqlSource;
    private String[] keyProperties;
    private String[] keyColumns;

    MappedStatement() {
        // constructor disabled
    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlSource sqlSource) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlSource = sqlSource;
        }

        public Builder resource(String resource) {
            mappedStatement.resource = resource;
            return this;
        }

        public String id() {
            return mappedStatement.id;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            assert mappedStatement.sqlSource != null;
            return mappedStatement;
        }
    }

    public String getResource() {
        return resource;
    }

//    public Configuration getConfiguration() {
//        return configuration;
//    }

    public String getId() {
        return id;
    }


    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public String[] getKeyProperties() {
        return keyProperties;
    }

    public String[] getKeyColumns() {
        return keyColumns;
    }

//    public BoundSql getBoundSql(Object parameterObject) {
//        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
//        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//        if (parameterMappings == null || parameterMappings.isEmpty()) {
//            boundSql = new BoundSql(configuration, boundSql.getSql(), parameterMap.getParameterMappings(), parameterObject);
//        }
//
//        // check for nested result maps in parameter mappings (issue #30)
//        for (ParameterMapping pm : boundSql.getParameterMappings()) {
//            String rmId = pm.getResultMapId();
//            if (rmId != null) {
//                ResultMap rm = configuration.getResultMap(rmId);
//                if (rm != null) {
//                    hasNestedResultMaps |= rm.hasNestedResultMaps();
//                }
//            }
//        }
//
//        return boundSql;
//    }
}
