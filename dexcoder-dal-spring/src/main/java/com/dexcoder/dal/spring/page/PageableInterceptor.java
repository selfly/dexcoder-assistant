package com.dexcoder.dal.spring.page;

import java.sql.DatabaseMetaData;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dexcoder.dal.page.*;

/**
 * spring 分页拦截器
 * 
 * Created by liyd on 16/10/28.
 */
@Aspect
public class PageableInterceptor {

    /** 数据库 */
    public static String   DATABASE;

    /** 分页sql处理器 */
    private PageSqlHandler pageSqlHandler;

    @Pointcut("execution(* org.springframework.jdbc.core.JdbcOperations.query*(..))")
    public void queryMethod() {
        //该方法没实际作用，只是切面声明对象，声明一个切面的表达式
        //下面使用时，只需要写入这个表达式名(方法名)即可   等同于
        //@Before("anyMethod()") == @Before("execution(* org.springframework.jdbc.core.JdbcOperations.query*(..))")
        //可以是private修饰符，但是会有never used的警告，所以这里用了public
    }

    @Around("queryMethod()")
    public Object pagerAspect(ProceedingJoinPoint pjp) throws Throwable {

        Pager pager = PageControl.getPager();
        if (pager == null) {
            return pjp.proceed();
        }

        JdbcTemplate target = (JdbcTemplate) pjp.getTarget();
        if (DATABASE == null) {
            DatabaseMetaData metaData = target.getDataSource().getConnection().getMetaData();
            DATABASE = metaData.getDatabaseProductName().toUpperCase();
        }

        Object[] args = pjp.getArgs();
        String querySql = (String) args[0];
        args[0] = this.getPageSqlHandler().getPageSql(querySql, pager, DATABASE);

        if (pager.isGetCount()) {
            String countSql = this.getPageSqlHandler().getCountSql(querySql, pager, DATABASE);
            Object[] countArgs = null;
            for (Object obj : args) {
                if (obj instanceof Object[]) {
                    countArgs = (Object[]) obj;
                }
            }
            int itemsTotal = target.queryForObject(countSql, countArgs, Integer.class);
            pager.setItems(itemsTotal);
        }
        Object result = pjp.proceed(args);

        return new PageList((List) result, pager);
        //        PageList<?> pageList = new PageList<?>();
        //        pager.setList((List<?>) result);
        //
        //        return result;
    }

    public PageSqlHandler getPageSqlHandler() {
        if (pageSqlHandler == null) {
            pageSqlHandler = new SimplePageSqlHandler();
        }
        return pageSqlHandler;
    }

    public void setPageSqlHandler(PageSqlHandler pageSqlHandler) {
        this.pageSqlHandler = pageSqlHandler;
    }
}
