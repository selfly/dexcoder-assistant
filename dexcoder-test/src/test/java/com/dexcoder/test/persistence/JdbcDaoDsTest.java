package com.dexcoder.test.persistence;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dexcoder.dal.JdbcDao;
import com.dexcoder.dal.build.Criteria;
import com.dexcoder.test.model.User;

/**
 * Created by liyd on 2016-1-27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ds-applicationContext.xml" })
public class JdbcDaoDsTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void dyDsInsert() {
        User user = new User();
        user.setLoginName("selfly");
        user.setGmtCreate(new Date());
        Long id = jdbcDao.insert(user);

        User u = jdbcDao.get(Criteria.select(User.class).include("userId", "loginName", "gmtCreate"), id);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " : " + u.getLoginName());
    }

    @Test
    public void dyDsGet() {
        User u = jdbcDao.get(Criteria.select(User.class).include("userId", "loginName", "gmtCreate"), 6L);
        Assert.assertNull(u);
    }

    @Test
    public void dyDsGet2() {
        List<Map<String, String>> dsList = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "dataSource4");
        map.put("class", "org.apache.commons.dbcp.BasicDataSource");
        map.put("default", "true");
        map.put("weight", "10");
        map.put("mode", "rw");
        map.put("driverClassName", "com.mysql.dal.Driver");
        map.put("url", "dal:mysql://localhost:3306/db1?useUnicode=true&amp;characterEncoding=utf-8");
        map.put("username", "root");
        map.put("password", "");
        dsList.add(map);

        int i = 0;
        while (i < 100) {
            User u = jdbcDao.get(Criteria.select(User.class).include("userId", "loginName", "gmtCreate"), 6L);
            System.out.println(u == null ? "null" : u.getLoginName());

            if (i == 70) {
                //                    dynamicDataSource.initDataSources(dsList);
            }
            i++;
        }
    }
}
