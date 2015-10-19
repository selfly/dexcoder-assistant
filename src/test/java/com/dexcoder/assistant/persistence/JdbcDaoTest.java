package com.dexcoder.assistant.persistence;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dexcoder.assistant.enums.UserType;
import com.dexcoder.assistant.interceptor.PageControl;
import com.dexcoder.assistant.model.User;
import com.dexcoder.assistant.test.BaseTest;

/**
 * Created by liyd on 3/3/15.
 */
public class JdbcDaoTest extends BaseTest {

    @Autowired
    private JdbcDao jdbcDao;

    @BeforeClass
    public static void before() {

    }

    @Test
    public void insertData() {
        //删除所有数据并插入50条测试数据
        jdbcDao.deleteAll(User.class);
        for (int i = 1; i < 51; i++) {
            User user = new User();
            user.setLoginName("selfly" + i);
            user.setPassword("123456");
            user.setEmail("javaer" + i + "@live.com");
            user.setUserAge(i);
            user.setUserType(UserType.MEMBER.getCode());
            user.setGmtCreate(new Date());
            Long id = jdbcDao.insert(user);
            System.out.println("insert:" + id);
        }
    }

    @Test
    public void insert() {
        User user = new User();
        user.setLoginName("selfly");
        user.setPassword("123456");
        user.setEmail("javaer@live.com");
        user.setUserAge(18);
        user.setUserType(UserType.MEMBER.getCode());
        user.setGmtCreate(new Date());
        Long id = jdbcDao.insert(user);
        System.out.println("insert:" + id);
    }

    @Test
    public void insert2() {
        Criteria criteria = Criteria.create(User.class).set("loginName", "selfly2criteria")
            .set("password", "12345678").set("email", "selflly@foxmail.com").set("userAge", 22)
            .set("userType", UserType.VIP.getCode()).set("gmtCreate", new Date());
        Long id = jdbcDao.insert(criteria);
        System.out.println("insert:" + id);
    }

    @Test
    public void save() {

        User user = new User();
        user.setUserId(-1L);
        user.setLoginName("selfly-1");
        user.setPassword("123456");
        user.setEmail("javaer@live.com");
        user.setUserAge(18);
        user.setUserType(UserType.MEMBER.getCode());
        user.setGmtCreate(new Date());
        jdbcDao.save(user);
    }

    @Test
    public void save2() {
        Criteria criteria = Criteria.create(User.class).set("userId", -2L)
            .set("loginName", "selfly-2").set("password", "12345678")
            .set("email", "selflly@foxmail.com").set("userAge", 22)
            .set("userType", UserType.VIP.getCode()).set("gmtCreate", new Date());
        jdbcDao.save(criteria);
    }

    @Test
    public void update() {
        User user = new User();
        user.setUserId(2L);
        user.setPassword("abcdef");
        user.setGmtModify(new Date());
        jdbcDao.update(user);
    }

    @Test
    public void update2() {
        Criteria criteria = Criteria.create(User.class).set("password", "update222")
            .where("userId", new Object[] { 5L, 6L, 7L });
        jdbcDao.update(criteria);
    }

    @Test
    public void delete() {
        User u = new User();
        u.setLoginName("selfly9");
        u.setUserType(UserType.MEMBER.getCode());
        jdbcDao.delete(u);
    }

    @Test
    public void delete2() {
        Criteria criteria = Criteria.create(User.class)
            .where("loginName", new Object[] { "selfly13" }).or("userAge", new Object[] { 33 });
        jdbcDao.delete(criteria);
    }

    @Test
    public void delete3() {
        jdbcDao.delete(User.class, 25L);
    }

    @Test
    public void queryList() {
        User u = new User();
        u.setUserType(UserType.MEMBER.getCode());
        List<User> users = jdbcDao.queryList(u);
        Assert.assertNotNull(users);
        for (User us : users) {
            System.out.println(us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList1() {

        List<User> users = jdbcDao.queryList(User.class);
        Assert.assertNotNull(users);
        for (User us : users) {
            System.out.println(us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList2() {
        PageControl.performPage(1, 2);
        Criteria criteria = Criteria.create(User.class).include("loginName", "userId")
            .asc("userId");
        jdbcDao.queryList(criteria);
        List<User> users = PageControl.getPager().getList(User.class);
        Assert.assertNotNull(users);
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList3() {
        Criteria criteria = Criteria.create(User.class).exclude("userId")
            .where("userType", new Object[] { UserType.MEMBER.getCode() }).asc("userAge")
            .desc("userId");
        List<User> users = jdbcDao.queryList(criteria);
        Assert.assertNotNull(users);
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList4() {
        Criteria criteria = Criteria.create(User.class).where("loginName", "like",
            new Object[] { "%selfly%" });
        User user1 = new User();
        user1.setUserType("1");
        List<User> users = jdbcDao.queryList(user1, criteria.include("userId"));
        Assert.assertNotNull(users);
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryCount() {
        User u = new User();
        u.setLoginName("selfly9");
        int count = jdbcDao.queryCount(u);
        System.out.println(count);
    }

    @Test
    public void queryCount2() {
        Criteria criteria = Criteria.create(User.class)
            .where("loginName", new Object[] { "selfly9" }).or("userAge", new Object[] { 27 });
        int count = jdbcDao.queryCount(criteria);
        System.out.println(count);
    }

    @Test
    public void get() {

        User u = jdbcDao.get(User.class, 32L);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());

    }

    @Test
    public void get2() {
        Criteria criteria = Criteria.create(User.class).include("loginName");
        User u = jdbcDao.get(criteria, 23L);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void querySingleResult() {
        User u = new User();
        u.setLoginName("selfly45");
        u.setUserType(UserType.MEMBER.getCode());
        u = jdbcDao.querySingleResult(u);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void querySingleResult2() {
        Criteria criteria = Criteria.create(User.class)
            .where("loginName", new Object[] { "selfly45" })
            .and("userType", new Object[] { UserType.MEMBER.getCode() });
        User u = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void testBracket() {

        Criteria criteria = Criteria.create(User.class)
            .where("userType", new Object[] { UserType.MEMBER.getCode() }).beginBracket()
            .and("loginName", new Object[] { "selfly1" })
            .or("email", new Object[] { "javaer1@live.com" }).endBracket()
            .and("password", new Object[] { "123456" });
        User user = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(user);
        System.out.println(user.getLoginName());
    }
}
