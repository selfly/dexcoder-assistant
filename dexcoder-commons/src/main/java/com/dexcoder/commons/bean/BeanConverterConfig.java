package com.dexcoder.commons.bean;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Bean 对象转换器配置
 * <p/>
 * User: liyd
 * Date: 13-5-10 下午1:56
 * version $Id: BeanConverterConfig.java, v 0.1 Exp $
 */
public class BeanConverterConfig {

    /** 包含 BeanConverter 的 ContextClassLoader 实例索引 */
    private static final ContextClassLoaderLocal      BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {

                                                                               // 创建默认的实例
                                                                               protected Object initialValue() {
                                                                                   return new BeanConverterConfig();
                                                                               }
                                                                           };

    /** 对象的转换器 */
    private SoftReference<Map<String, TypeConverter>> converters           = new SoftReference<Map<String, TypeConverter>>(
                                                                               new HashMap<String, TypeConverter>());

    /**
     * 获取实例，提供的功能应用于 {@link BeanConverter}.
     * 这是一个伪单例 - 每一个线程的ContextClassLoader提供一个单例的实例
     * 这种机制提供了在同一个web容器中部署的应用程序之间的隔离
     *
     * @return 该伪单例的实例 BeanConverterConfig
     */
    public static BeanConverterConfig getInstance() {
        BeanConverterConfig beanConverterConfig = (BeanConverterConfig) BEANS_BY_CLASSLOADER.get();
        return beanConverterConfig;
    }

    /**
     * 设置实例，提供的功能应用于 {@link BeanConverter}.
     * 这是一个伪单例 - 每一个线程的ContextClassLoader提供一个单例的实例
     * 这种机制提供了在同一个web容器中部署的应用程序之间的隔离
     *
     * @param newInstance 该伪单例的实例 BeanConverterConfig
     */
    public static void setInstance(BeanConverterConfig newInstance) {
        BEANS_BY_CLASSLOADER.set(newInstance);
    }

    /**
     * 获取转换器key
     * 
     * @param sourceClass
     * @param targetClass
     * @return
     */
    public static String getCovertKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.getName() + "_" + targetClass.getName();
    }

    /**
     * 注册转换器
     *
     * @param converter the converter
     */
    public void registerConverter(TypeConverter converter) {
        Map<String, TypeConverter> map = converters.get();
        if (map == null) {
            converters = new SoftReference<Map<String, TypeConverter>>(map = new HashMap<String, TypeConverter>());
        }
        map.put(BeanConverterConfig.getCovertKey(converter.getSourceTypeClass(), converter.getTargetTypeClass()),
            converter);
    }

    /**
     * 移除注册的转换器
     *
     * @param sourceClass the source class
     * @param targetClass the target class
     */
    public void unregisterConverter(Class<?> sourceClass, Class<?> targetClass) {
        Map<String, TypeConverter> map = converters.get();
        if (map == null) {
            return;
        }
        map.remove(BeanConverterConfig.getCovertKey(sourceClass, targetClass));
    }

    /**
     * 获取所有转换器
     * 
     * @return
     */
    public Map<String, TypeConverter> getConverters() {
        return converters.get();
    }

    /**
     * 清空注册的转换器
     */
    public void clearConverter() {
        this.converters.clear();
    }

}
