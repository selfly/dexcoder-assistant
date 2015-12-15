package com.dexcoder.dal.batis.xml;

import com.dexcoder.dal.batis.build.DynamicContext;

/**
 * Created by liyd on 2015-11-25.
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
