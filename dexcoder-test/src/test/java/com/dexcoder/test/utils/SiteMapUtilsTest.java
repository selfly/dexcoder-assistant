package com.dexcoder.test.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 2015-12-15.
 */
public class SiteMapUtilsTest {

    @Test
    public void ttt() {

        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("loc", "http://www.oschina.net/code/explore/.repo");
        map.put("priority", "0.6");
        mapList.add(map);
//        SiteMapUtils.createSiteMapXml(mapList, "d:/aaa.xml");

    }

}
