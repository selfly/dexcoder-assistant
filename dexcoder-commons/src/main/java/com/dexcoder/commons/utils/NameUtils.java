package com.dexcoder.commons.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 名称操作辅助类
 * <p/>
 * User: liyd
 * Date: 13-12-6
 * Time: 下午5:17
 */
public class NameUtils {

    /**
     * 首字母大写
     *
     * @param name
     * @return
     */
    public static String getFirstUpperName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String firstChar = StringUtils.substring(name, 0, 1).toUpperCase();
        return firstChar + StringUtils.substring(name, 1);
    }

    /**
     * 首字母小写
     *
     * @param name
     * @return
     */
    public static String getFirstLowerName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String firstChar = StringUtils.substring(name, 0, 1).toLowerCase();
        return firstChar + StringUtils.substring(name, 1);
    }

    public static void main(String[] args) {
        System.out.println(NameUtils.getCamelName("login-name-info", '-'));
        System.out.println(NameUtils.getCamelName("LOGIN-NAME-INFO", '-'));
        System.out.println(NameUtils.getCamelName("-login-name-info-", '-'));
        System.out.println(NameUtils.getCamelName("-LOGIN-NAME-INFO-", '-'));
    }

    /**
     * 转换成骆驼命名法返回 默认分隔符下划线_
     *
     * @param name
     * @return
     */
    public static String getCamelName(String name) {
        return getCamelName(name, '_');
    }

    /**
     * 转换成骆驼命名法返回,指定分隔符
     *
     * @param name
     * @return
     */
    public static String getCamelName(String name, char delimiter) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        name = StringUtils.lowerCase(name);
        //去掉前面的 delimiter
        while (name.charAt(0) == delimiter) {
            name = StringUtils.substring(name, 1);
        }
        //去掉后面的 delimiter
        while (name.charAt(name.length() - 1) == delimiter) {
            name = StringUtils.substring(name, 0, name.length() - 1);
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {

            char c = name.charAt(i);

            if (c == delimiter) {
                i++;
                sb.append(Character.toUpperCase(name.charAt(i)));
                continue;
            }
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 将骆驼命名法反转成下划线返回
     *
     * @param name
     * @return
     */
    public static String getUnderlineName(String name) {

        return getUpperDelimiterName(name, "_");
    }

    /**
     * 将骆驼命名法反转成下划线返回
     *
     * @param name
     * @return
     */
    public static String getLineThroughName(String name) {

        return getLowerDelimiterName(name, "-");
    }

    /**
     * 返回大写的按指定分隔符命名
     *
     * @param name
     * @param delimiter
     * @return
     */
    public static String getUpperDelimiterName(String name, String delimiter) {
        return getLowerDelimiterName(name, delimiter).toUpperCase();
    }

    /**
     * 返回小写的按指定分隔符命名
     *
     * @param name
     * @return
     */
    public static String getLowerDelimiterName(String name, String delimiter) {

        if (StringUtils.isBlank(name)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {

            char c = name.charAt(i);

            if (i > 0 && Character.isUpperCase(c)) {
                sb.append(delimiter);
            }

            sb.append(c);
        }

        return sb.toString().toLowerCase();
    }

    /**
     * 保留原文件后缀生成唯一文件名
     *
     * @param fileName
     * @return
     */
    public static String createUniqueFileName(String fileName) {

        int index = StringUtils.lastIndexOf(fileName, ".");
        String suffix = StringUtils.substring(fileName, index);
        String uqName = UUIDUtils.getUUID16() + suffix;
        return uqName;
    }

    /**
     * 在文件名后加上指定后缀，不包括后缀名
     *
     * @param fileName
     * @param endSuffix
     * @return
     */
    public static String createEndSuffixFileName(String fileName, String endSuffix) {
        int index = StringUtils.lastIndexOf(fileName, ".");
        String preFileName = StringUtils.substring(fileName, 0, index);
        String suffix = StringUtils.substring(fileName, index);
        return preFileName + endSuffix + suffix;
    }

}
