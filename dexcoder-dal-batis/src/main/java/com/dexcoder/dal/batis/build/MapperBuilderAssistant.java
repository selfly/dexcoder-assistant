package com.dexcoder.dal.batis.build;

import com.dexcoder.dal.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-11-24.
 */
public class MapperBuilderAssistant extends BaseBuilder {

    private String currentNamespace;
    private String resource;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public void setCurrentNamespace(String currentNamespace) {
        if (currentNamespace == null) {
            throw new JdbcAssistantException("The mapper element requires a namespace attribute to be specified.");
        }

        if (this.currentNamespace != null && !this.currentNamespace.equals(currentNamespace)) {
            throw new JdbcAssistantException("Wrong namespace. Expected '" + this.currentNamespace + "' but found '"
                                             + currentNamespace + "'.");
        }

        this.currentNamespace = currentNamespace;
    }

    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference) {
            // is it qualified with any namespace yet?
            if (base.contains(".")) {
                return base;
            }
        } else {
            // is it qualified with this namespace yet?
            if (base.startsWith(currentNamespace + ".")) {
                return base;
            }
            if (base.contains(".")) {
                throw new JdbcAssistantException("Dots are not allowed in element names, please remove it from " + base);
            }
        }
        return currentNamespace + "." + base;
    }

    public MappedStatement addMappedStatement(String id, SqlSource sqlSource) {

        id = applyCurrentNamespace(id, false);
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource);
        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement(statement);
        return statement;
    }

    public String getResource(){
    	return resource;
    }
}
