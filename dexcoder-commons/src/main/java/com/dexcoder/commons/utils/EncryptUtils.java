package com.dexcoder.commons.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * Created by liyd on 7/2/14.
 */
public class EncryptUtils {

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {

        try {

            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            //当数字以0开头时会去掉0，补齐
            for (int i = 32; i > md5.length(); i--) {
                md5 = "0" + md5;
            }
            return md5;
        } catch (Exception e) {
            throw new CommonsAssistantException("MD5加密出现错误", e);
        }
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] hmacSHA1Encrypt(String encryptText, String encryptKey) {
        try {
            byte[] data = encryptKey.getBytes();
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = encryptText.getBytes();
            //完成 Mac 操作
            return mac.doFinal(text);
        } catch (Exception e) {
            throw new CommonsAssistantException("HmacSHA1加密出现错误", e);
        }
    }
}
