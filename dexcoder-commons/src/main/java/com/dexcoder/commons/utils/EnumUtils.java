package com.dexcoder.commons.utils;

import com.dexcoder.commons.enums.IEnum;
import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * 枚举辅助类
 * 
 * User: liyd
 * Date: 14-1-25
 * Time: 上午10:17
 */
public final class EnumUtils {

    /**
     * 获取枚举的所有属性
     * 
     * @param clazz
     * @return
     */
    public static IEnum[] getEnums(Class<?> clazz) {
        if (IEnum.class.isAssignableFrom(clazz)) {
            Object[] enumConstants = clazz.getEnumConstants();
            return (IEnum[]) enumConstants;
        }
        return null;
    }

    /**
     * 获取枚举的所有属性
     * 
     * @param enumClass
     * @return
     */
    public static IEnum[] getEnums(String enumClass) {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(enumClass);
            return getEnums(clazz);
        } catch (ClassNotFoundException e) {
            throw new CommonsAssistantException("加载枚举类失败", e);
        }
    }

    /**
     * 获取枚举的所有属性
     *
     * @param clazz the clazz
     * @param code the code
     * @return enum
     */
    @SuppressWarnings("unchecked")
    public static <T> T getEnum(Class<T> clazz, String code) {
        if (!IEnum.class.isAssignableFrom(clazz)) {
            return null;
        }
        IEnum[] enumConstants = (IEnum[]) clazz.getEnumConstants();
        for (IEnum enumConstant : enumConstants) {
            if (enumConstant.getCode().equalsIgnoreCase(code)) {
                return (T) enumConstant;
            }
        }
        return null;
    }

    /**
     * 获取枚举的所有属性
     *
     * @param clazzName the clazzName
     * @param code the code
     * @return enum
     */
    @SuppressWarnings("unchecked")
    public static <T> T getEnum(String clazzName, String code) {
        Class<?> clazz = ClassUtils.loadClass(clazzName);
        if (!IEnum.class.isAssignableFrom(clazz)) {
            return null;
        }
        IEnum[] enumConstants = (IEnum[]) clazz.getEnumConstants();
        for (IEnum enumConstant : enumConstants) {
            if (enumConstant.getCode().equalsIgnoreCase(code)) {
                return (T) enumConstant;
            }
        }
        return null;
    }
}
