package com.dexcoder.dal.batis.xml;

import com.dexcoder.dal.batis.build.BaseBuilder;
import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.build.MapperBuilderAssistant;
import com.dexcoder.dal.batis.build.SqlSource;

/**
 * Created by liyd on 2015-11-24.
 */
public class XMLStatementBuilder extends BaseBuilder {

    private MapperBuilderAssistant builderAssistant;
    private XNode                  xNode;

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

        //        SqlSource sqlSource = new XMLLanguageDriver().createSqlSource(configuration, xNode);

        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, xNode);
        SqlSource sqlSource = builder.parseScriptNode();
        builderAssistant.addMappedStatement(id, sqlSource);
    }
}
