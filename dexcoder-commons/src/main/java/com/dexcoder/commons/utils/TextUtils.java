package com.dexcoder.commons.utils;

import java.io.UnsupportedEncodingException;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * 字符文本操作
 * 太多的StringUtils了，命名为TextUtils
 * <p/>
 * Created by liyd on 2015-8-14.
 */
public class TextUtils {

    /**
     * 转换特殊符号
     *
     * @param str
     * @return
     */
    public static String convertHtmlSpecialChars(String str) {
        if (StrUtils.isBlank(str)) {
            return null;
        }
        //最后一个中文全角空格换成英文，防止strin的trim方法失效
        String[][] chars = new String[][] { { "&", "&amp;" }, { "<", "&lt;" }, { ">", "&gt;" }, { "\"", "&quot;" },
                { "　", " " } };
        return replaceChars(str, chars);
    }

    /**
     * 反转特殊符号，将转义后的符号转换回标签，以便缩进等格式化
     *
     * @param str
     * @return
     */
    public static String reverseHtmlSpecialChars(String str) {
        if (StrUtils.isBlank(str)) {
            return null;
        }
        String[][] chars = new String[][] { { "&amp;", "&" }, { "&lt;", "<" }, { "&gt;", ">" }, { "&quot;", "\"" },
                { "　", " " } };
        return replaceChars(str, chars);
    }

    public static String replaceChars(String str, String[][] chars) {
        for (String[] cs : chars) {
            str = str.replace(cs[0], cs[1]);
        }
        return str;
    }

    /**
     * 截取字符串，按byte长度，可以避免直接按length截取中英文混合显示长短差很多的情况
     *
     * @param text
     * @param length
     * @return
     */
    public static String substringForByte(String text, int length) {

        return substringForByte(text, length, true);
    }

    /**
     * 截取字符串，按byte长度，可以避免直接按length截取中英文混合显示长短差很多的情况
     *
     * @param text
     * @param length
     * @return
     */
    public static String substringForByte(String text, int length, boolean isConvertSpecialChars) {

        if (StrUtils.isBlank(text) || length < 1) {
            return text;
        }
        try {
            //防止中英文有长有短，转换成byte截取
            byte[] bytes = text.getBytes("GBK");

            //截取
            byte[] contentNameBytes = ArrUtils.subarray(bytes, 0, length);

            //处理截取了半个汉字的情况
            int count = 0;
            for (byte b : contentNameBytes) {
                if (b < 0) {
                    count++;
                }
            }
            if (count % 2 != 0) {
                contentNameBytes = ArrUtils.subarray(contentNameBytes, 0, contentNameBytes.length - 1);
            }

            String contentName = new String(contentNameBytes, "GBK");
            //转换特殊字符，页面显示时非常有用
            if (isConvertSpecialChars) {
                contentName = convertHtmlSpecialChars(contentName);
            }
            return contentName;
        } catch (UnsupportedEncodingException e) {
            throw new CommonsAssistantException("根据byte截取字符串失败", e);
        }
    }
}
