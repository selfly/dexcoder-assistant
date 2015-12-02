package com.dexcoder.assistant.persistence.manual;

import java.util.List;

/**
 * Created by liyd on 2015-11-25.
 */
public class MixedSqlNode implements SqlNode {
    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    public boolean apply(DynamicContext context) {
        for (SqlNode sqlNode : contents) {
            sqlNode.apply(context);
        }
        return true;
    }
}