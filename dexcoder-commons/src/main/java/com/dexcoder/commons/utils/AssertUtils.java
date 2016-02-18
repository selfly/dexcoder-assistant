package com.dexcoder.commons.utils;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import com.dexcoder.commons.enums.IEnum;
import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * Created by liyd on 2015-8-24.
 */
public class AssertUtils {

    public static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertNotNull(Object obj, IEnum iEnum) {
        if (obj == null) {
            throw new CommonsAssistantException(iEnum);
        }
    }

    public static void assertNotEquals(String str1, String str2, String message) {
        if (!StrUtils.equals(str1, str2)) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertNotEquals(String str1, String str2, IEnum message) {
        if (!StrUtils.equals(str1, str2)) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertNotBlank(String str, String message) {
        if (StrUtils.isBlank(str)) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertNotBlank(String str, IEnum message) {
        if (StrUtils.isBlank(str)) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertNotEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertEmpty(Collection<?> collection, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertMinLength(String str, int length, String message) {
        if (StrUtils.length(str) < length) {
            throw new CommonsAssistantException(message);
        }
    }

    public static void assertRegex(String str, String regex, String message) {
        AssertUtils.assertNotBlank(str, message);
        boolean matches = str.matches(regex);
        if (!matches) {
            throw new CommonsAssistantException(message);
        }
    }
}
