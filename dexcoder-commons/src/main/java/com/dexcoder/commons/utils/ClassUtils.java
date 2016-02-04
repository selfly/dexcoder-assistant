package com.dexcoder.commons.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * 类辅助
 * <p/>
 * User: liyd
 * Date: 2/12/14
 * Time: 10:08 PM
 */
public class ClassUtils {

    /**
     * Map keyed by class containing CachedIntrospectionResults.
     * Needs to be a WeakHashMap with WeakReferences as values to allow
     * for proper garbage collection in case of multiple class loaders.
     */
    private static final Map<Class<?>, BeanInfo> CLASS_CACHE = Collections
                                                                 .synchronizedMap(new WeakHashMap<Class<?>, BeanInfo>());

    /**
     * 获取类本身的BeanInfo，不包含父类属性
     *
     * @param clazz
     * @return
     */
    public static BeanInfo getBeanInfo(Class<?> clazz, Class<?> stopClazz) {
        try {
            BeanInfo beanInfo;
            if (CLASS_CACHE.get(clazz) == null) {
                beanInfo = Introspector.getBeanInfo(clazz, stopClazz);
                CLASS_CACHE.put(clazz, beanInfo);
                // Immediately remove class from Introspector cache, to allow for proper
                // garbage collection on class loader shutdown - we cache it here anyway,
                // in a GC-friendly manner. In contrast to CachedIntrospectionResults,
                // Introspector does not use WeakReferences as values of its WeakHashMap!
                Class<?> classToFlush = clazz;
                do {
                    Introspector.flushFromCaches(classToFlush);
                    classToFlush = classToFlush.getSuperclass();
                } while (classToFlush != null);
            } else {
                beanInfo = CLASS_CACHE.get(clazz);
            }
            return beanInfo;
        } catch (IntrospectionException e) {
            throw new CommonsAssistantException("获取BeanInfo失败", e);
        }
    }

    /**
     * 获取类的BeanInfo,包含父类属性
     *
     * @param clazz
     * @return
     */
    public static BeanInfo getBeanInfo(Class<?> clazz) {

        return getBeanInfo(clazz, Object.class);
    }

    /**
     * 获取类本身的BeanInfo，不包含父类属性
     *
     * @param clazz
     * @return
     */
    public static BeanInfo getSelfBeanInfo(Class<?> clazz) {

        return getBeanInfo(clazz, clazz.getSuperclass());
    }

    /**
     * 获取类属性的PropertyDescriptor
     *
     * @param clazz
     * @param name
     * @return
     */
    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String name) {
        BeanInfo beanInfo = getBeanInfo(clazz);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (propertyDescriptors == null) {
            return null;
        }
        for (PropertyDescriptor pd : propertyDescriptors) {
            if (StrUtils.equals(pd.getName(), name)) {
                return pd;
            }
        }
        return null;
    }

    /**
     * bean属性转换为map
     *
     * @param object
     * @return
     */
    public static Map<String, Object> getBeanPropMap(Object object) {

        BeanInfo beanInfo = getBeanInfo(object.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (propertyDescriptors == null) {
            return null;
        }
        Map<String, Object> propMap = new HashMap<String, Object>();
        for (PropertyDescriptor pd : propertyDescriptors) {

            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Object value = invokeMethod(readMethod, object);
            propMap.put(pd.getName(), value);
        }
        return propMap;
    }

    /**
     * invokeMethod
     *
     * @param method
     * @param bean
     * @param value
     */
    public static void invokeMethod(Method method, Object bean, Object value) {
        try {
            methodAccessible(method);
            method.invoke(bean, value);
        } catch (Exception e) {
            throw new CommonsAssistantException("执行invokeMethod失败:" + (method == null ? "null" : method.getName()), e);
        }
    }

    /**
     * invokeMethod
     *
     * @param method
     * @param bean
     */
    public static Object invokeMethod(Method method, Object bean) {
        try {
            methodAccessible(method);
            return method.invoke(bean);
        } catch (Exception e) {
            throw new CommonsAssistantException("执行invokeMethod失败:" + (method == null ? "null" : method.getName()), e);
        }
    }

    /**
     * 设置method访问权限
     * 
     * @param method
     */
    public static void methodAccessible(Method method) {
        if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }

    /**
     * 初始化实例
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new CommonsAssistantException("根据class创建实例失败:" + (clazz == null ? "null" : clazz.getName()), e);
        }
    }

    /**
     * 初始化实例
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(String clazz) {

        try {
            Class<?> loadClass = getDefaultClassLoader().loadClass(clazz);
            return loadClass.newInstance();
        } catch (Exception e) {
            throw new CommonsAssistantException("根据class创建实例失败:" + clazz, e);
        }
    }

    /**
     * 加载类
     *
     * @param clazz
     * @return
     */
    public static Class<?> loadClass(String clazz) {
        try {
            Class<?> loadClass = getDefaultClassLoader().loadClass(clazz);
            return loadClass;
        } catch (Exception e) {
            throw new CommonsAssistantException("根据class名称加载class失败:" + clazz, e);
        }
    }

    /**
     * 将value的数据类型转换到实际目标类型
     * 
     * @param value
     * @return
     */
    public static Object toTargetTypeValue(Object value, Class<?> targetType) {
        String typeName = targetType.getName();
        if (StrUtils.equals(typeName, boolean.class.getName()) || StrUtils.equals(typeName, Boolean.class.getName())) {
            return Boolean.valueOf(value.toString());
        }
        if (StrUtils.equals(typeName, int.class.getName()) || StrUtils.equals(typeName, Integer.class.getName())) {
            return Integer.valueOf(value.toString());
        }
        if (StrUtils.equals(typeName, long.class.getName()) || StrUtils.equals(typeName, Long.class.getName())) {
            return Long.valueOf(value.toString());
        }
        if (StrUtils.equals(typeName, short.class.getName()) || StrUtils.equals(typeName, Short.class.getName())) {
            return Short.valueOf(value.toString());
        }
        if (StrUtils.equals(typeName, float.class.getName()) || StrUtils.equals(typeName, Float.class.getName())) {
            return Float.valueOf(value.toString());
        }
        if (StrUtils.equals(typeName, double.class.getName()) || StrUtils.equals(typeName, Double.class.getName())) {
            return Double.valueOf(value.toString());
        }
        if (StrUtils.equals(typeName, byte.class.getName()) || StrUtils.equals(typeName, Byte.class.getName())) {
            return Byte.valueOf(value.toString());
        }
        return value;
    }

    /**
     * 当前线程的classLoader
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
