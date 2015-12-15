package com.dexcoder.dal;

import java.util.List;

/**
 * 具体的sql、参数信息对象
 * <p/>
 * Created by liyd on 2015-12-4.
 */
public interface BoundSql {

    //    /**
    //     * 获取主键名称
    //     *
    //     * @return
    //     */
    //    String getPkName();

    /**
     * 获取要执行的sql，参数的地方用?号与下面参数列表对应
     *
     * @return
     */
    String getSql();

    /**
     * 获取执行sql的参数，与上面sql中的?号对应
     *
     * @return
     */
    List<Object> getParameters();

}
