package com.dexcoder.assistant.persistence;

import java.util.Date;
import java.util.List;

import com.dexcoder.assistant.interceptor.PageControl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dexcoder.assistant.test.BaseTest;

/**
 * Created by liyd on 3/3/15.
 */
public class JdbcDaoTest extends BaseTest {

    @Autowired
    private JdbcDao jdbcDao;
    private User    user;

    @Before
    public void before() {
        user = new User();
        user.setLoginName("liyd");
        user.setUserType("1");
        user.setGmtCreate(new Date());
    }

    @Test
    public void insert() {
        Long id = jdbcDao.insert(user);
        System.out.println(id);
    }

    @Test
    public void insert2() {
        Criteria criteria = Criteria.create(User.class).set("loginName", "liyd22")
            .set("userAge", 22).set("gmtCreate", new Date());
        Long id = jdbcDao.insert(criteria);
        System.out.println(id);
    }

    @Test
    public void save() {
        user.setUserId(-123L);
        jdbcDao.save(user);
    }

    @Test
    public void save2() {
        Criteria criteria = Criteria.create(User.class).set("userId", -122L)
            .set("loginName", "liyd22").set("userAge", 22).set("gmtCreate", new Date())
            .set("gmtModify", new Date());
        jdbcDao.save(criteria);
    }

    @Test
    public void update() {
        user.setUserId(34L);
        user.setLoginName("liyd34");
        user.setGmtCreate(null);
        user.setGmtModify(new Date());
        jdbcDao.update(user);
    }

    @Test
    public void update2() {
        Criteria criteria = Criteria.create(User.class).set("loginName", "liydCriteria")
            .set("userAge", "18").where("userId", new Object[] { 34L, 33L, 32L });
        jdbcDao.update(criteria);
    }

    @Test
    public void delete() {
        User u = new User();
        u.setLoginName("selfly");
        u.setUserType("1");
        jdbcDao.delete(u);
    }

    @Test
    public void delete2() {
        Criteria criteria = Criteria.create(User.class)
            .where("loginName", new Object[] { "liyd2" }).or("userAge", new Object[] { 64 });
        jdbcDao.delete(criteria);
    }

    @Test
    public void delete3() {
        jdbcDao.delete(User.class, 25L);
    }

    @Test
    public void queryList() {
        User u = new User();
        u.setLoginName("liyd");
        List<User> users = jdbcDao.queryList(u);
        for (User us : users) {
            System.out.println(us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList1() {

        List<User> users = jdbcDao.queryList(Criteria.create(User.class));
        for (User us : users) {
            System.out.println(us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList2() {
        PageControl.performPage(1, 2);
        Criteria criteria = Criteria.create(User.class).include("loginName", "userId")
            .where("loginName", new Object[] { "liyd" }).asc("userId");
        jdbcDao.queryList(criteria);
        List<User> users = PageControl.getPager().getList(User.class);
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList3() {
        Criteria criteria = Criteria.create(User.class).exclude("userId")
            .where("loginName", new Object[] { "liyd" }).asc("userId").desc("userAge");
        List<User> users = jdbcDao.queryList(criteria);
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList4() {
        Criteria criteria = Criteria.create(User.class).where("loginName", "like",
            new Object[] { "%liyd%" });
        User user1 = new User();
        user1.setUserType("1");
        List<User> users = jdbcDao.queryList(user1, criteria.include("userId"));
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList5() {
        List<User> users = jdbcDao.queryList(Criteria.create(User.class));
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryCount() {
        User u = new User();
        u.setLoginName("liyd");
        int count = jdbcDao.queryCount(u);
        System.out.println(count);
    }

    @Test
    public void queryCount2() {
        Criteria criteria = Criteria.create(User.class).where("loginName", new Object[] { "liyd" })
            .or("userAge", new Object[] { 27 });
        int count = jdbcDao.queryCount(criteria);
        System.out.println(count);
    }

    @Test
    public void get() {

        User u = jdbcDao.get(User.class, 23L);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());

    }

    @Test
    public void get2() {
        Criteria criteria = Criteria.create(User.class).include("loginName");
        User u = jdbcDao.get(criteria, 23L);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void querySingleResult() {
        User u = new User();
        u.setLoginName("liyd");
        u.setUserType("1");
        u.setUserId(23L);
        u = jdbcDao.querySingleResult(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void querySingleResult2() {
        Criteria criteria = Criteria.create(User.class)
            .where("loginName", new Object[] { "liyd2" }).and("userId", new Object[] { 10008L });
        User u = jdbcDao.querySingleResult(criteria);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void testBracket() {

        Criteria criteria = Criteria.create(User.class)
            .where("userType", new Object[] { "MEMBER" }).beginBracket()
            .and("loginName", new Object[] { "selfly" })
            .or("email", new Object[] { "javaer@live.com" }).endBracket()
            .and("password", new Object[] { "123456" });
        User user = jdbcDao.querySingleResult(criteria);
        System.out.println(user.getLoginName());
    }
}
