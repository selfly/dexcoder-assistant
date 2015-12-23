package com.dexcoder.dal;

import java.util.List;

/**
 * 具体的sql、参数信息对象
 * <p/>
 * Created by liyd on 2015-12-4.
 */
public interface BoundSql {

    /**
     * Get execute sql.
     * Parameter with a ? mark and corresponds below parameters.
     *
     * @return
     */
    String getSql();

    /**
     * Get execute sql parameters.
     * Allowed to return empty list,Don't return null.
     *
     * @return
     */
    List<Object> getParameters();

}
