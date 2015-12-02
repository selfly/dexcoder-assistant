package com.dexcoder.assistant.persistence.manual;

import java.util.*;

/**
 * Created by liyd on 2015-11-24.
 */
public class Configuration {

    protected final Set<String> loadedResources = new HashSet<String>();

    protected final Map<String, MappedStatement> mappedStatements = new HashMap<String, MappedStatement>();

    protected final Map<String, XNode> sqlFragments = new HashMap<String, XNode>();

    protected Properties variables = new Properties();

    public Map<String, XNode> getSqlFragments() {
        return sqlFragments;
    }

    public Map<String, MappedStatement> getMappedStatements() {
        return mappedStatements;
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public Properties getVariables() {
        return variables;
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object);
    }
}
