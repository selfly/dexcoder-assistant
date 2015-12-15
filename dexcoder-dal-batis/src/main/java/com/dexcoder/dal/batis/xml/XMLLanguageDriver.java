package com.dexcoder.dal.batis.xml;

import com.dexcoder.dal.batis.build.Configuration;
import com.dexcoder.dal.batis.build.SqlSource;

/**
 * 只有xml 去掉
 * Created by liyd on 2015-11-25.
 */
@Deprecated
public class XMLLanguageDriver {

    public SqlSource createSqlSource(Configuration configuration, XNode script) {
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script);
        return builder.parseScriptNode();
    }

    //    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
    //        // issue #3
    //        if (script.startsWith("<script>")) {
    //            XPathParser handler = new XPathParser(script, false, configuration.getVariables(), new XMLMapperEntityResolver());
    //            return createSqlSource(configuration, handler.evalNode("/script"), parameterType);
    //        } else {
    //            // issue #127
    //            script = PropertyParser.parse(script, configuration.getVariables());
    //            TextSqlNode textSqlNode = new TextSqlNode(script);
    //            if (textSqlNode.isDynamic()) {
    //                return new DynamicSqlSource(configuration, textSqlNode);
    //            } else {
    //                return new RawSqlSource(configuration, script, parameterType);
    //            }
    //        }
    //    }

}
