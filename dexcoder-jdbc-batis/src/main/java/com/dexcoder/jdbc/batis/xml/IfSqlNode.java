package com.dexcoder.jdbc.batis.xml;

import com.dexcoder.jdbc.batis.build.DynamicContext;
import com.dexcoder.jdbc.batis.ognl.ExpressionEvaluator;

/**
 * Created by liyd on 2015-11-30.
 */
public class IfSqlNode implements SqlNode {
    private ExpressionEvaluator evaluator;
    private String test;
    private SqlNode contents;

    public IfSqlNode(SqlNode contents, String test) {
        this.test = test;
        this.contents = contents;
        this.evaluator = new ExpressionEvaluator();
    }

    public boolean apply(DynamicContext context) {
        if (evaluator.evaluateBoolean(test, context.getBindings())) {
            contents.apply(context);
            return true;
        }
        return false;
    }

}
