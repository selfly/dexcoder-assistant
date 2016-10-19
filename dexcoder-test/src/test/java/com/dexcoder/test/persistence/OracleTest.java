package com.dexcoder.test.persistence;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dexcoder.dal.JdbcDao;
import com.dexcoder.test.model.UserInfo;

/**
 * Created by liyd on 16/10/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:oracle-applicationContext.xml" })
public class OracleTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void insert() {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("selfly");
        userInfo.setPassword("123456");
        userInfo.setEmail("selfly@foxmail.com");
        userInfo.setAge(20);
        userInfo.setGmtCreate(new Date());

        Object id = jdbcDao.insert(userInfo);

        System.out.println(id);
        //返回的是序列的值,而非序列名
        Assert.assertTrue(id instanceof Long);
    }
}
