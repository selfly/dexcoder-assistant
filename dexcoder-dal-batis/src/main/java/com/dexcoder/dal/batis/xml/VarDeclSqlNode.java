package com.dexcoder.dal.batis.xml;

import com.dexcoder.dal.batis.ognl.OgnlCache;
import com.dexcoder.dal.batis.build.DynamicContext;

/**
 * Created by liyd on 2015-11-30.
 */
public class VarDeclSqlNode implements SqlNode {

    private final String name;
    private final String expression;

    public VarDeclSqlNode(String var, String exp) {
        name = var;
        expression = exp;
    }

    public boolean apply(DynamicContext context) {
        final Object value = OgnlCache.getValue(expression, context.getBindings());
        context.bind(name, value);
        return true;
    }

}
