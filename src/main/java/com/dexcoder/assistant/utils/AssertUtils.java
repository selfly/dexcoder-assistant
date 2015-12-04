package com.dexcoder.assistant.utils;

import com.dexcoder.assistant.enums.IEnum;
import com.dexcoder.assistant.exceptions.AssistantException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by liyd on 2015-8-24.
 */
public class AssertUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AssertUtils.class);

    public static void assertNull(Object obj, String message) {
        if (obj == null) {
            throw new AssistantException(message);
        }
    }

    public static void assertNull(Object obj, IEnum iEnum) {
        if (obj == null) {
            throw new AssistantException(iEnum);
        }
    }

    public static void assertEquals(String str1, String str2, String message) {
        if (!StrUtils.equals(str1, str2)) {
            throw new AssistantException(message);
        }
    }

    public static void assertBlank(String str, String message) {
        if (StrUtils.isBlank(str)) {
            throw new AssistantException(message);
        }
    }

    public static void assertEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new AssistantException(message);
        }
    }

    public static void assertNotEmpty(Collection<?> collection, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new AssistantException(message);
        }
    }

    public static void assertMinLength(String str, int length, String message) {
        if (StrUtils.length(str) < length) {
            throw new AssistantException(message);
        }
    }

    public static void assertRegex(String str, String regex, String message) {
        AssertUtils.assertBlank(str, message);
        boolean matches = str.matches(regex);
        if (!matches) {
            throw new AssistantException(message);
        }
    }
}
