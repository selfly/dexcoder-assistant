package com.dexcoder.commons.utils;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by liyd on 7/30/14.
 */
public class TimeUtils {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    /**
     * 获取当天开始时间
     *
     * @return
     */
    public static Date getTodayBegin() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return currentDate.getTime();
    }

    /**
     * 获取当天结束时间
     *
     * @return
     */
    public static Date getTodayEnd() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTime();
    }

    /**
     * 格式化成一行
     *
     * @param date
     * @return
     */
    public static String formatOne(Date date) {
        return format(date, "one");
    }

    /**
     * 格式化成两行
     *
     * @param date
     * @return
     */
    public static String formatTwo(Date date) {
        return format(date, "two");
    }

    //时间转换
    public static String format(Date date, String line) {

        if (date == null) {
            return "未知";
        }

        long curTime = System.currentTimeMillis() - date.getTime();

        if (curTime < 1L * ONE_MINUTE) {
            long seconds = toSeconds(curTime);
            return (seconds <= 0 ? 1 : seconds) + getAgo(ONE_SECOND_AGO, line);
        }
        if (curTime < 45L * ONE_MINUTE) {
            long minutes = toMinutes(curTime);
            return (minutes <= 0 ? 1 : minutes) + getAgo(ONE_MINUTE_AGO, line);
        }
        if (curTime < 24L * ONE_HOUR) {
            long hours = toHours(curTime);
            return (hours <= 0 ? 1 : hours) + getAgo(ONE_HOUR_AGO, line);
        }
        if (curTime < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (curTime < 30L * ONE_DAY) {
            long days = toDays(curTime);
            return (days <= 0 ? 1 : days) + getAgo(ONE_DAY_AGO, line);
        }
        if (curTime < 12L * 4L * ONE_WEEK) {
            long months = toMonths(curTime);
            return (months <= 0 ? 1 : months) + getAgo(ONE_MONTH_AGO, line);
        } else {
            long years = toYears(curTime);
            return (years <= 0 ? 1 : years) + getAgo(ONE_YEAR_AGO, line);
        }
    }

    /**
     * 格式化换行
     *
     * @param ago
     * @param line
     * @return
     */
    public static String getAgo(String ago, String line) {

        if (StrUtils.equals("one", line)) {
            return ago;
        }
        if (StrUtils.equals("two", line)) {
            return "<br>" + ago;
        }

        return null;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

}
