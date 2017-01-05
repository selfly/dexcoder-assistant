package com.dexcoder.test.utils;

import com.dexcoder.commons.utils.PropertyUtils;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

/**
 * Created by liyd on 17/1/5.
 */
public class PropertyUtilsTest {

    @Test
    public void getProperties() {

        Map<String, String> properties = PropertyUtils.getProperties("init");
        for (Entry<String, String> entry : properties.entrySet()) {

            System.out.println(entry.getKey() + " : " + entry.getValue());

        }

        System.out.println("=======================");

        Map<String, String> properties2 = PropertyUtils.getProperties("oracle-init");
        for (Entry<String, String> entry : properties2.entrySet()) {

            System.out.println(entry.getKey() + " : " + entry.getValue());

        }

        System.out.println("==========================");

        String property = PropertyUtils.getProperty("init", "driverClassName");
        System.out.println(property);

        String driverClassName = PropertyUtils.getProperty("oracle-init", "driverClassName");
        System.out.println(driverClassName);

    }

}
