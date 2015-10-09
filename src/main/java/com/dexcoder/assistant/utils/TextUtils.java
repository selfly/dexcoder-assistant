package com.dexcoder.assistant.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexcoder.assistant.exceptions.DexcoderException;

/**
 * 字符文本操作
 * 太多的StringUtils了，命名为TextUtils
 * 
 * Created by liyd on 2015-8-14.
 */
public class TextUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TextUtils.class);

    /**
     * 转换特殊符号
     *
     * @param str
     * @return
     */
    public static String convertSpecialChars(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        //中文全角空格换成英文，防止strin的trim方法失效
        str = str.replaceAll("　", " ");
        return str;
    }

    /**
     * 反转特殊符号，将转义后的符号转换回标签，以便缩进等格式化
     *
     * @param str
     * @return
     */
    public static String reverseSpecialChars(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        str = str.replaceAll("&quot;", "\"");
        //中文全角空格换成英文，防止strin的trim方法失效
        str = str.replaceAll("　", " ");
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

        if (StringUtils.isBlank(text) || length < 1) {
            return text;
        }
        try {
            //防止中英文有长有短，转换成byte截取
            byte[] bytes = text.getBytes("GBK");

            //截取
            byte[] contentNameBytes = ArrayUtils.subarray(bytes, 0, length);

            //处理截取了半个汉字的情况
            int count = 0;
            for (byte b : contentNameBytes) {
                if (b < 0) {
                    count++;
                }
            }
            if (count % 2 != 0) {
                contentNameBytes = ArrayUtils.subarray(contentNameBytes, 0,
                    contentNameBytes.length - 1);
            }

            String contentName = new String(contentNameBytes, "GBK");
            //转换特殊字符，页面显示时非常有用
            if (isConvertSpecialChars) {
                contentName = convertSpecialChars(contentName);
            }
            return contentName;
        } catch (UnsupportedEncodingException e) {
            LOG.error("根据byte截取字符串失败", e);
            throw new DexcoderException("根据byte截取字符串失败");
        }
    }
}
