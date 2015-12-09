/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2012 All Rights Reserved.
 */
package com.dexcoder.commons.utils;

import java.io.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * 序列化辅助类
 *
 * @author liyd
 * @version $Id: SerializerUtil.java, v 0.1 2012-8-16 下午4:11:08 liyd Exp $
 */
public final class SerializeUtils {

    private SerializeUtils() {
    }

    /**
     * 将对象序列化成字符串
     *
     * @param obj
     * @return
     */
    public static String objectToString(Object obj) {

        if (obj == null) {
            return null;
        }

        ByteArrayOutputStream baops = null;
        ObjectOutputStream oos = null;
        try {
            baops = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baops);
            oos.writeObject(obj);

            //产生编码问题，用base64保证完整性
            return Base64.encodeBase64String(baops.toByteArray());

        } catch (IOException e) {
            throw new CommonsAssistantException("将对象序列化成字符串失败", e);
        } finally {
            IOUtils.closeQuietly(baops);
            IOUtils.closeQuietly(oos);
        }
    }

    /**
     * 将字符串反序列化成对象
     *
     * @param strObj
     * @return
     */
    public static Object stringToObject(String strObj) {

        if (StrUtils.isBlank(strObj)) {
            return null;
        }

        ObjectInputStream ois = null;

        try {
            byte[] bytes = strObj.getBytes();
            ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(Base64.decodeBase64(bytes))));

            Object obj = ois.readObject();
            return obj;
        } catch (Exception e) {
            throw new CommonsAssistantException("将字符串反序列化成对象失败", e);
        } finally {
            IOUtils.closeQuietly(ois);
        }

    }
}
