package com.dexcoder.test.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dexcoder.dal.JdbcDao;
import com.dexcoder.test.model.User;

/**
 * mybatis方式 对应sql文件:user-sql.xml
 * 这里主要看如何传参及使用参数，其它和mybatis一样
 * 
 * Created by liyd on 2016-1-13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:batis-applicationContext.xml" })
public class JdbcDaoBatisTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void batisInsert() {
        //login_name,password,user_age,user_type,email,gmt_create
        int i = jdbcDao.updateForSql("User.insertUser", new Object[] { "batis_user", "123456", 18, "1",
                "batis@dexcoder.com", new Date() });
        Assert.assertEquals(i, 1);
    }

    @Test
    public void batisInsert2() {
        User user = new User();
        user.setLoginName("batisUser2");
        user.setPassword("123456");
        user.setUserAge(19);
        user.setUserType("1");
        user.setEmail("batis2@dexcoder.com");
        user.setGmtCreate(new Date());
        int i = jdbcDao.updateForSql("User.insertUser2", "user", new Object[] { user });
        Assert.assertEquals(i, 1);
    }

    @Test
    public void batisInsert3() {
        int i = jdbcDao.updateForSql("User.insertUser3");
        Assert.assertEquals(i, 1);
    }

    @Test
    public void batisInsert4() {
        //login_name,password,user_age,user_type,email,gmt_create
        int i = jdbcDao.updateForSql("User.insertUser4", new Object[] { "batis_user4", "123456", 18, "1",
                "batis@dexcoder.com", new Date() });
        Assert.assertEquals(i, 1);
    }

    @Test
    public void batisSelect() {
        this.batisInsert2();
        User user = new User();
        user.setLoginName("batisUser2");
        List<Map<String, Object>> mapList = jdbcDao.queryListForSql("User.getUser", new Object[] { "1", user });
        Assert.assertNotNull(mapList);
        for (Map<String, Object> map : mapList) {
            Assert.assertEquals("1", map.get("USER_TYPE"));
            Assert.assertEquals("batisUser2", map.get("LOGIN_NAME"));
        }
    }

}
