package com.dexcoder.assistant.persistence.manual;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liyd on 2015-11-30.
 */
public class WhereSqlNode extends TrimSqlNode {

    private static List<String> prefixList = Arrays.asList("AND ", "OR ", "AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");

    public WhereSqlNode(Configuration configuration, SqlNode contents) {
        super(configuration, contents, "WHERE", prefixList, null, null);
    }

}