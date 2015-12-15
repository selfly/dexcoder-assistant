package com.dexcoder.dal.spring.datasource;

/**
 * 动态数据源切换标识
 * <p/>
 * Created by liyd on 2015-11-2.
 */
public class DynamicDataSourceHolder {

    private static final ThreadLocal<DataSourceContext> DATASOURCE_LOCAL = new ThreadLocal<DataSourceContext>();

    /**
     * 设置数据源读写模式
     *
     * @param isWrite
     */
    public static void setIsWrite(boolean isWrite) {
        DataSourceContext dsContext = DATASOURCE_LOCAL.get();
        //已经持有且可写，直接返回
        if (dsContext != null && dsContext.getIsWrite()) {
            return;
        }

        if (dsContext == null || isWrite) {
            dsContext = new DataSourceContext();
            dsContext.setIsWrite(isWrite);
            DATASOURCE_LOCAL.set(dsContext);
        }
    }

    /**
     * 获取dsKey
     *
     * @return
     */
    public static DataSourceContext getDsContent() {
        return DATASOURCE_LOCAL.get();
    }


    /**
     * 清除
     */
    public static void clear() {
        DATASOURCE_LOCAL.remove();
    }
}
