package com.dexcoder.dal.batis.xml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dexcoder.dal.batis.build.BaseBuilder;
import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.build.MapperBuilderAssistant;
import com.dexcoder.dal.batis.parser.ClassFieldHandler;
import com.dexcoder.dal.exceptions.JdbcAssistantException;
import com.dexcoder.dal.handler.DefaultMappingHandler;
import com.dexcoder.dal.handler.GenericTokenParser;
import com.dexcoder.dal.handler.MappingHandler;

/**
 * Created by liyd on 2015-11-24.
 */
public class XMLMapperBuilder extends BaseBuilder {

    private XPathParser            parser;
    private MapperBuilderAssistant builderAssistant;
    private Map<String, XNode>     sqlFragments;
    private String                 resource;
    private MappingHandler         mappingHandler;

    public XMLMapperBuilder(Resource resource, Configuration configuration, MappingHandler mappingHandler)
                                                                                                          throws IOException {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource.getFilename());
        this.sqlFragments = configuration.getSqlFragments();
        this.resource = resource.getFilename();
        this.mappingHandler = mappingHandler;
        this.parser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(),
            new XMLMapperEntityResolver());
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
            sqlElement(context.evalNodes("/mapper/sql"));
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
        GenericTokenParser tokenParser = new GenericTokenParser("${", "}", new ClassFieldHandler(this.mappingHandler));
        for (XNode xNode : list) {
            String id = xNode.getStringAttribute("id");
            id = builderAssistant.applyCurrentNamespace(id, false);

            Node node = xNode.getNode();
            this.processSql(node, tokenParser);

            sqlFragments.put(id, xNode);
        }
    }

    private void processSql(Node node, GenericTokenParser tokenParser) {

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            processSql(item, tokenParser);
            if (item.getNodeType() != Node.TEXT_NODE) {
                continue;
            }
            String result = tokenParser.parse(item.getTextContent().trim());
            item.setTextContent(result);
        }
    }
}
