package com.dexcoder.dal.handler;

import java.io.Serializable;

/**
 * Created by liyd on 16/8/25.
 */
public interface KeyGenerator {

    /**
     * 主键值是否来自sql
     * true:类似于oracle的序列,下面的generateKeyValue方法只是返回了序列名,真正的主键值是sql执行时获取的序列值
     * false:如UUID主键,下面的generateKeyValue方法返回一个UUID,这个UUID就已经是实际的主键值
     * 
     * @return
     */
    boolean isPkValueBySql();

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
