package com.dexcoder.test.bean;

import org.junit.Assert;
import org.junit.Test;

import com.dexcoder.commons.bean.BeanKit;
import com.dexcoder.test.model.User;
import com.dexcoder.test.model.UserVo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyd on 2015-6-4.
 */
public class BeanKitTest {

    @Test
    public void convert() {

        UserVo user = new UserVo();
        user.setUserId(1000L);
        user.setLoginName("liyd");

        User userVo = BeanKit.convert(new User(), user);

        System.out.println(userVo.getLoginName());

    }

    @Test
    public void map2Bean() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", 10000L);
        map.put("loginName", "loginName");
        map.put("userAge", 8);
        map.put("gmtCreate", new Date());

        map.put("userQQ", "12345678");

        User user = BeanKit.mapToBean(map, User.class);

        Assert.assertTrue(user.getUserId().equals(10000L));
        Assert.assertEquals(user.getLoginName(), "loginName");
        Assert.assertTrue(user.getUserAge().equals(8));
        Assert.assertTrue(user.getGmtCreate() != null);

        Assert.assertTrue(user.get("userQQ").equals("12345678"));

    }

    @Test
    public void map2Bean2() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_Id", 10000L);
        map.put("login_Name", "loginName");
        map.put("user_Age", 8);
        map.put("gmt_Create", new Date());

        User user = BeanKit.underlineKeyMapToBean(map, User.class);

        Assert.assertTrue(user.getUserId().equals(10000L));
        Assert.assertEquals(user.getLoginName(), "loginName");
        Assert.assertTrue(user.getUserAge().equals(8));
        Assert.assertTrue(user.getGmtCreate() != null);
    }

    @Test
    public void map2Bean3() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user-Id", 10000L);
        map.put("login-Name", "loginName");
        map.put("user-Age", 8);
        map.put("gmt-Create", new Date());

        User user = BeanKit.mapToBean(map, User.class,'-');

        Assert.assertTrue(user.getUserId().equals(10000L));
        Assert.assertEquals(user.getLoginName(), "loginName");
        Assert.assertTrue(user.getUserAge().equals(8));
        Assert.assertTrue(user.getGmtCreate() != null);
    }
}
