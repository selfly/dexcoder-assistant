package com.dexcoder.commons.page;

import org.aspectj.lang.annotation.Aspect;

import com.dexcoder.commons.page.Pageable;

/**
 * 分页拦截器
 *
 * Created by liyd on 6/26/14.
 */
@Aspect
public class PageControl {

    /** 分页线程变量 */
    public static final ThreadLocal<Pager> LOCAL_PAGER = new ThreadLocal<Pager>();

    /**
     * 执行分页
     *
     * @param pageable
     */
    public static void performPage(Pageable pageable) {
        performPage(pageable.getCurPage(), pageable.getItemsPerPage(), true);
    }

    /**
     * 执行分页
     *
     * @param pageable
     */
    public static void performPage(Pageable pageable, boolean isGetCount) {
        performPage(pageable.getCurPage(), pageable.getItemsPerPage(), isGetCount);
    }

    /**
     * 执行分页
     *
     * @param curPage
     * @param itemsPerPage
     */
    public static void performPage(int curPage, int itemsPerPage) {
        performPage(curPage, itemsPerPage, true);
    }

    /**
     * 执行分页
     *
     * @param curPage
     * @param itemsPerPage
     * @param isGetCount
     */
    public static void performPage(int curPage, int itemsPerPage, boolean isGetCount) {
        Pager pager = new Pager();
        pager.setPage(curPage);
        pager.setItemsPerPage(itemsPerPage);
        pager.setGetCount(isGetCount);
        LOCAL_PAGER.set(pager);
    }

    /**
     * 获取Pager对象,默认获取后清除
     * 
     * @return
     */
    public static Pager getPager() {
        return getPager(true);
    }

    /**
     * 获取Pager对象,
     *
     * @param isClear false则默认后不清除
     * @return pager
     */
    public static Pager getPager(boolean isClear) {
        Pager pager = LOCAL_PAGER.get();
        //获取数据时清除
        if (isClear) {
            LOCAL_PAGER.remove();
        }
        return pager;
    }

}
