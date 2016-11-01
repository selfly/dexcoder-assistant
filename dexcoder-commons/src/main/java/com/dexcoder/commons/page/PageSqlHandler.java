package com.dexcoder.commons.page;

/**
 * Created by liyd on 16/6/8.
 */
public interface PageSqlHandler {

    /**
     * 根据查询sql获取count sql
     * 
     * @param sql
     * @param pager
     * @param database
     * @return
     */
    String getCountSql(String sql, Pager pager, String database);

    /**
     * 根据查询sql获取分页sql
     * 
     * @param sql
     * @param pager
     * @param database
     * @return
     */
    String getPageSql(String sql, Pager pager, String database);
}
