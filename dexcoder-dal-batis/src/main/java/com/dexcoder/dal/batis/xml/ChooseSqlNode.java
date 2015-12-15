package com.dexcoder.dal.batis.xml;

import com.dexcoder.dal.batis.build.DynamicContext;

import java.util.List;

/**
 * Created by liyd on 2015-11-30.
 */
public class ChooseSqlNode implements SqlNode {
    private SqlNode defaultSqlNode;
    private List<SqlNode> ifSqlNodes;

    public ChooseSqlNode(List<SqlNode> ifSqlNodes, SqlNode defaultSqlNode) {
        this.ifSqlNodes = ifSqlNodes;
        this.defaultSqlNode = defaultSqlNode;
    }

    public boolean apply(DynamicContext context) {
        for (SqlNode sqlNode : ifSqlNodes) {
            if (sqlNode.apply(context)) {
                return true;
            }
        }
        if (defaultSqlNode != null) {
            defaultSqlNode.apply(context);
            return true;
        }
        return false;
    }
}
