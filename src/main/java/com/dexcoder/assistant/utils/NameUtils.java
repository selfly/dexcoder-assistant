package com.dexcoder.assistant.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 名称操作辅助类
 * 
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

    /**
     * 转换成骆驼命名法返回
     * 
     * @param name
     * @return
     */
    public static String getCamelName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        name = StringUtils.lowerCase(name);
        //去掉前面的_
        while (StringUtils.startsWith(name, "_")) {
            name = StringUtils.substring(name, 1);
        }
        //去掉后面的_
        while (StringUtils.endsWith(name, "_")) {
            name = StringUtils.substring(name, 0, name.length() - 1);
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {

            char c = name.charAt(i);

            if (c == '_') {
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

        if (StringUtils.isBlank(name)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {

            char c = name.charAt(i);

            if (i > 0 && Character.isUpperCase(c)) {
                sb.append("_");
            }

            sb.append(c);
        }

        return sb.toString().toUpperCase();
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
