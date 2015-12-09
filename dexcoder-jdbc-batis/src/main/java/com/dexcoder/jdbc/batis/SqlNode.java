package com.dexcoder.jdbc.batis;

/**
 * Created by liyd on 2015-11-25.
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
