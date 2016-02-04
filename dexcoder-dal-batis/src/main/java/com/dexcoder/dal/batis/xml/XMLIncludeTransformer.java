package com.dexcoder.dal.batis.xml;

import java.util.Properties;

import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.parser.PropertyParser;
import com.dexcoder.dal.batis.build.MapperBuilderAssistant;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dexcoder.dal.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-11-24.
 */
public class XMLIncludeTransformer {

    private final Configuration configuration;
    private final MapperBuilderAssistant builderAssistant;

    public XMLIncludeTransformer(Configuration configuration, MapperBuilderAssistant builderAssistant) {
        this.configuration = configuration;
        this.builderAssistant = builderAssistant;
    }

    public void applyIncludes(Node source) {
        Properties variablesContext = new Properties();
        Properties configurationVariables = configuration.getVariables();
        if (configurationVariables != null) {
            variablesContext.putAll(configurationVariables);
        }
        applyIncludes(source, variablesContext);
    }

    /**
     * Recursively apply includes through all SQL fragments.
     *
     * @param source           Include node in DOM tree
     * @param variablesContext Current context for static variables with values
     */
    private void applyIncludes(Node source, final Properties variablesContext) {
        if (source.getNodeName().equals("include")) {
            // new full context for included SQL - contains inherited context and new variables from current include node
            Properties fullContext;

            String refId = getStringAttribute(source, "refid");
            // replace variables in include refid value
            refId = PropertyParser.parse(refId, variablesContext);
            Node toInclude = findSqlFragment(refId);
            Properties newVariablesContext = getVariablesContext(source, variablesContext);
            if (!newVariablesContext.isEmpty()) {
                // merge contexts
                fullContext = new Properties();
                fullContext.putAll(variablesContext);
                fullContext.putAll(newVariablesContext);
            } else {
                // no new context - use inherited fully
                fullContext = variablesContext;
            }
            applyIncludes(toInclude, fullContext);
            if (toInclude.getOwnerDocument() != source.getOwnerDocument()) {
                toInclude = source.getOwnerDocument().importNode(toInclude, true);
            }
            source.getParentNode().replaceChild(toInclude, source);
            while (toInclude.hasChildNodes()) {
                toInclude.getParentNode().insertBefore(toInclude.getFirstChild(), toInclude);
            }
            toInclude.getParentNode().removeChild(toInclude);

            //           DocUtils.toStringFromDoc(source.getOwnerDocument()) ;
        } else if (source.getNodeType() == Node.ELEMENT_NODE) {
            NodeList children = source.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                applyIncludes(children.item(i), variablesContext);
            }
        } else if (source.getNodeType() == Node.ATTRIBUTE_NODE && !variablesContext.isEmpty()) {
            // replace variables in all attribute values
            source.setNodeValue(PropertyParser.parse(source.getNodeValue(), variablesContext));
        } else if (source.getNodeType() == Node.TEXT_NODE && !variablesContext.isEmpty()) {
            // replace variables ins all text nodes
            source.setNodeValue(PropertyParser.parse(source.getNodeValue(), variablesContext));
        }
    }

    private Node findSqlFragment(String refid) {
        refid = builderAssistant.applyCurrentNamespace(refid, true);
        try {
            XNode nodeToInclude = configuration.getSqlFragments().get(refid);
            return nodeToInclude.getNode().cloneNode(true);
        } catch (Exception e) {
            throw new JdbcAssistantException("Could not find SQL statement to include with refid '" + refid + "'", e);
        }
    }

    private String getStringAttribute(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getNodeValue();
    }

    /**
     * Read placholders and their values from include node definition.
     *
     * @param node                      Include node instance
     * @param inheritedVariablesContext Current context used for replace variables in new variables values
     * @return variables context from include instance (no inherited values)
     */
    private Properties getVariablesContext(Node node, Properties inheritedVariablesContext) {
        Properties variablesContext = new Properties();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String name = getStringAttribute(n, "name");
                String value = getStringAttribute(n, "value");
                // Replace variables inside
                value = PropertyParser.parse(value, inheritedVariablesContext);
                // Push new value
                Object originalValue = variablesContext.put(name, value);
                if (originalValue != null) {
                    throw new JdbcAssistantException("Variable " + name
                                                     + " defined twice in the same include definition");
                }
            }
        }
        return variablesContext;
    }

}
