package com.dexcoder.jdbc.batis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.dexcoder.jdbc.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-11-24.
 */
public class XMLMapperBuilder extends BaseBuilder {

    private XPathParser            parser;
    private MapperBuilderAssistant builderAssistant;
    private Map<String, XNode>     sqlFragments;
    private String                 resource;

    public XMLMapperBuilder(Resource resource, Configuration configuration) throws IOException {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource.getFilename());
        this.sqlFragments = configuration.getSqlFragments();
        this.resource = resource.getFilename();
        this.parser = new XPathParser(resource.getInputStream());
    }

    public void parse() {
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(parser.evalNode("/mapper"));
            configuration.addLoadedResource(resource);
        }
    }

    private void configurationElement(XNode context) {
        try {
            String namespace = context.getStringAttribute("namespace");
            if (namespace == null || namespace.equals("")) {
                throw new JdbcAssistantException("Mapper's namespace cannot be empty");
            }
            builderAssistant.setCurrentNamespace(namespace);
            sqlElement(context.evalNodes("/mapper/build"));
            buildStatementFromContext(context.evalNodes("select|insert|update|delete"));
        } catch (Exception e) {
            throw new JdbcAssistantException("Error parsing Mapper XML. Cause: " + e, e);
        }
    }

    private void buildStatementFromContext(List<XNode> list) {
        for (XNode xNode : list) {
            final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, xNode);
            statementParser.parseStatementNode();
        }
    }

    private void sqlElement(List<XNode> list) throws Exception {
        for (XNode xNode : list) {
            String id = xNode.getStringAttribute("id");
            id = builderAssistant.applyCurrentNamespace(id, false);
            sqlFragments.put(id, xNode);
        }
    }
}
