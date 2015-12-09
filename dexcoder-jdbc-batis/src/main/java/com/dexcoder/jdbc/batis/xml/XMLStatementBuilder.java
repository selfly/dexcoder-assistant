package com.dexcoder.jdbc.batis.xml;

import com.dexcoder.jdbc.batis.build.BaseBuilder;
import com.dexcoder.jdbc.batis.build.Configuration;
import com.dexcoder.jdbc.batis.build.MapperBuilderAssistant;
import com.dexcoder.jdbc.batis.build.SqlSource;

/**
 * Created by liyd on 2015-11-24.
 */
public class XMLStatementBuilder extends BaseBuilder {

    private MapperBuilderAssistant builderAssistant;
    private XNode xNode;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode xNode) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.xNode = xNode;
    }

    public void parseStatementNode() {
        String id = xNode.getStringAttribute("id");

        // Include Fragments before parsing
        XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
        includeParser.applyIncludes(xNode.getNode());

        // Parse the SQL (pre: <selectKey> and <include> were parsed and removed)
        SqlSource sqlSource = new XMLLanguageDriver().createSqlSource(configuration, xNode);
//        SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
        builderAssistant.addMappedStatement(id, sqlSource);
    }
}
