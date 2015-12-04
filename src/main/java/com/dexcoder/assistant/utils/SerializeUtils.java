/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2012 All Rights Reserved.
 */
package com.dexcoder.assistant.utils;

import com.dexcoder.assistant.exceptions.AssistantException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 序列化辅助类
 *
 * @author liyd
 * @version $Id: SerializerUtil.java, v 0.1 2012-8-16 下午4:11:08 liyd Exp $
 */
public final class SerializeUtils {

    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(SerializeUtils.class);

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
            LOG.error("将对象序列化成字符串失败", e);
            throw new AssistantException("将对象序列化成字符串失败");
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
            ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(
                    Base64.decodeBase64(bytes))));

            Object obj = ois.readObject();
            return obj;
        } catch (Exception e) {
            LOG.error("将字符串反序列化成对象失败", e);
            throw new AssistantException("将字符串反序列化成对象失败");
        } finally {
            IOUtils.closeQuietly(ois);
        }

    }
}
