package com.dexcoder.dal.handler;

import java.io.Serializable;

/**
 * Created by liyd on 16/8/25.
 */
public interface KeyGenerator {

    /**
     * 处理主键名(跟原生执行符号一致 {pkFieldName}field和value都原生执行 [pkFieldName]field会进行转换,value原生执行)
     *
     * @param pkFieldName the pk field name
     * @param dialect the dialect
     * @return string
     */
    String handlePkFieldName(String pkFieldName, String dialect);

    /**
     * 生成主键值
     *
     * @param clazz the clazz
     * @param dialect the dialect
     * @return serializable serializable
     */
    Serializable generateKeyValue(Class<?> clazz, String dialect);

}
