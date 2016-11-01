package com.dexcoder.commons.page;

/**
 * Created by liyd on 16/6/8.
 */
public class SimplePageSqlHandler implements PageSqlHandler {

    public String getCountSql(String sql, Pager pager, String database) {
        return new StringBuilder("select count(*) from (").append(sql).append(") tmp_count").toString();
    }

    public String getPageSql(String sql, Pager pager, String database) {
        StringBuilder pageSql = new StringBuilder(200);
        if ("MYSQL".equals(database)) {
            pageSql.append(sql);
            pageSql.append(" limit ");
            pageSql.append(pager.getBeginIndex());
            pageSql.append(",");
            pageSql.append(pager.getItemsPerPage());
        } else if ("ORACLE".equals(database)) {
            pageSql.append("select * from ( select rownum num,temp.* from (");
            pageSql.append(sql);
            pageSql.append(") temp where rownum <= ").append(pager.getEndIndex());
            pageSql.append(") where num > ").append(pager.getBeginIndex());
        }
        return pageSql.toString();
    }
}
