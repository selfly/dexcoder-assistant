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

import com.dexcoder.commons.pager.Pager;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.JdbcDao;
import com.dexcoder.dal.build.Criteria;
import com.dexcoder.dal.spring.page.PageControl;
import com.dexcoder.test.model.AnnotationUser;
import com.dexcoder.test.model.User;

/**
 * Created by liyd on 3/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class JdbcDaoTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void before() {
        //插入测试数据
        for (int i = 0; i < 15; i++) {
            User user = new User();
            String loginName = i % 2 == 0 ? "selfly_" : "liyd_";
            user.setLoginName(loginName + i);
            user.setPassword("123456");
            user.setEmail(i + "javaer@live.com");
            user.setUserAge(10 + i);
            String userType = i % 3 == 0 ? "1" : "2";
            user.setUserType(userType);
            user.setGmtCreate(new Date());
            Long id = jdbcDao.insert(user);
            Assert.assertNotNull(id);
        }

    }

    @Test
    public void insert() {
        User user = new User();
        user.setLoginName("selfly_12");
        user.setPassword("123456");
        user.setEmail("javaer@live.com");
        user.setUserAge(18);
        user.setUserType("1");
        user.setGmtCreate(new Date());
        Long id = jdbcDao.insert(user);
        Assert.assertNotNull(id);
    }

    @Test
    public void insert2() {
        Criteria criteria = Criteria.insert(User.class).set("loginName", "selfly_b").set("password", "12345678")
            .set("email", "selflly@foxmail.com").set("userAge", 22).set("userType", "2").set("gmtCreate", new Date());
        Long id = jdbcDao.insert(criteria);
        Assert.assertNotNull(id);
        System.out.println("insert:" + id);
    }

    @Test
    public void save() {
        //先删除存在的测试数据
        jdbcDao.delete(User.class, -2L);
        User user = jdbcDao.get(User.class, -2L);
        Assert.assertNull(user);

        user = new User();
        user.setUserId(-2L);
        user.setLoginName("selfly-2");
        user.setPassword("123456");
        user.setEmail("javaer@live.com");
        user.setUserAge(18);
        user.setUserType("1");
        user.setGmtCreate(new Date());
        jdbcDao.save(user);

        user = jdbcDao.get(User.class, -2L);
        Assert.assertNotNull(user);
    }

    @Test
    public void save2() {
        //先删除存在的测试数据
        jdbcDao.delete(User.class, -3L);
        User user = jdbcDao.get(User.class, -3L);
        Assert.assertNull(user);

        Criteria criteria = Criteria.insert(User.class).set("userId", -3L).set("loginName", "selfly-3")
            .set("password", "123456").set("email", "selfly@foxmail.com").set("userAge", 18).set("userType", "2")
            .set("gmtCreate", new Date());
        jdbcDao.save(criteria);

        user = jdbcDao.get(User.class, -3L);
        Assert.assertNotNull(user);
    }

    @Test
    public void update() {
        //插入测试数据
        this.save();

        User user = new User();
        user.setUserId(-2L);
        user.setPassword("update");
        int i = jdbcDao.update(user);
        Assert.assertEquals(i, 1);

        user = jdbcDao.get(User.class, -2L);
        Assert.assertEquals("update", user.getPassword());
    }

    @Test
    public void update2() {
        //插入测试数据
        this.save();
        this.save2();
        Criteria criteria = Criteria.update(User.class).set("password", "update2")
            .where("userId", new Object[] { -2L, -3L });
        int i = jdbcDao.update(criteria);
        Assert.assertEquals(i, 2);

        User user = jdbcDao.get(User.class, -2L);
        Assert.assertEquals("update2", user.getPassword());

        user = jdbcDao.get(User.class, -3L);
        Assert.assertEquals("update2", user.getPassword());
    }

    @Test
    public void update3() {
        this.save();
        User user = jdbcDao.get(User.class, -2L);
        Integer oldUserAge = user.getUserAge();

        Criteria criteria = Criteria.update(User.class).set("[userAge]", "[userAge]+1")
            .where("userId", new Object[] { -2L });
        int i = jdbcDao.update(criteria);
        Assert.assertEquals(i, 1);

        user = jdbcDao.get(User.class, -2L);
        Assert.assertEquals(oldUserAge + 1L, (long) user.getUserAge());
    }

    @Test
    public void update4() {
        this.save();
        User user = jdbcDao.get(User.class, -2L);
        Integer oldUserAge = user.getUserAge();

        Criteria criteria = Criteria.update(User.class).set("{USER_AGE}", "{USER_AGE + 1}")
            .where("userId", new Object[] { -2L });
        int i = jdbcDao.update(criteria);
        Assert.assertEquals(i, 1);

        user = jdbcDao.get(User.class, -2L);
        Assert.assertEquals(oldUserAge + 1L, (long) user.getUserAge());
    }

    @Test
    public void testUpdate5() {
        this.save();
        User u = new User();
        u.setUserId(-2L);
        u.setLoginName("aabb");
        int i = jdbcDao.update(u, false);
        Assert.assertEquals(1, i);

        User user = jdbcDao.get(User.class, -2L);
        Assert.assertNotNull(user.getUserId());
        Assert.assertNotNull(user.getLoginName());
        Assert.assertNull(user.getPassword());
        Assert.assertNull(user.getEmail());
        Assert.assertNull(user.getGmtCreate());
        Assert.assertNull(user.getUserType());
        Assert.assertNull(user.getUserAge());
    }

    @Test
    public void get() {

        this.save();
        User u = jdbcDao.get(User.class, -2L);
        Assert.assertNotNull(u);
        Assert.assertEquals(-2L, (long) u.getUserId());
    }

    @Test
    public void get2() {
        this.save();
        Criteria criteria = Criteria.select(User.class).include("loginName");
        User u = jdbcDao.get(criteria, -2L);
        Assert.assertNotNull(u);
        Assert.assertNotNull(u.getLoginName());
        Assert.assertNull(u.getPassword());
        Assert.assertNull(u.getEmail());
        Assert.assertNull(u.getGmtCreate());
        Assert.assertNull(u.getUserType());
        Assert.assertNull(u.getUserAge());
    }

    @Test
    public void delete() {
        this.save();
        User u = new User();
        u.setUserId(-2L);
        u.setLoginName("selfly-2");
        u.setUserType("1");
        int i = jdbcDao.delete(u);
        Assert.assertEquals(i, 1);
    }

    @Test
    public void delete2() {
        this.save();
        this.save2();
        int i = jdbcDao.delete(Criteria.delete(User.class).where("userId", "in", new Object[] { -2L, -3L }));
        Assert.assertEquals(i, 2);
    }

    @Test
    public void delete3() {
        this.save();
        int i = jdbcDao.delete(User.class, -2L);
        Assert.assertEquals(i, 1);
    }

    @Test
    public void queryList() {
        this.save();
        this.save2();
        User u = new User();
        u.setUserType("1");
        List<User> users = jdbcDao.queryList(u);
        Assert.assertNotNull(users);
        for (User us : users) {
            Assert.assertEquals("1", us.getUserType());
        }
    }

    @Test
    public void queryList1() {
        this.save();
        this.save2();
        List<User> users = jdbcDao.queryList(User.class);
        Assert.assertNotNull(users);
        int count = jdbcDao.queryCount(User.class);
        Assert.assertEquals(users.size(), count);
    }

    @Test
    public void queryList2() {
        this.save();
        this.save2();
        PageControl.performPage(1, 2);
        Criteria criteria = Criteria.select(User.class).include("loginName", "userId").asc("userId");
        jdbcDao.queryList(criteria);
        Pager pager = PageControl.getPager();
        List<User> users = pager.getList(User.class);
        Assert.assertNotNull(users);

        int count = jdbcDao.queryCount(User.class);
        Assert.assertEquals(pager.getItemsTotal(), count);

        for (User us : users) {
            Assert.assertNotNull(us.getLoginName());
            Assert.assertNotNull(us.getUserId());
            Assert.assertNull(us.getEmail());
            Assert.assertNull(us.getPassword());
            Assert.assertNull(us.getUserAge());
            Assert.assertNull(us.getUserType());
            Assert.assertNull(us.getGmtCreate());
        }

        Assert.assertTrue(users.get(0).getUserId() < users.get(1).getUserId());
    }

    @Test
    public void queryList3() {
        this.save();
        this.save2();
        Criteria criteria = Criteria.select(User.class).exclude("loginName").where("userType", new Object[] { "1" })
            .asc("userAge").desc("userId");
        List<User> users = jdbcDao.queryList(criteria);
        Assert.assertNotNull(users);
        for (User us : users) {
            Assert.assertNull(us.getLoginName());
            Assert.assertNotNull(us.getEmail());
            Assert.assertNotNull(us.getUserId());
            Assert.assertNotNull(us.getUserType());
            Assert.assertNotNull(us.getUserAge());
            Assert.assertNotNull(us.getGmtCreate());
        }

        Assert.assertTrue(users.get(0).getUserId() > users.get(1).getUserId());
    }

    @Test
    public void queryList4() {
        this.save();
        this.save2();
        Criteria criteria = Criteria.select(User.class).where("loginName", "like", new Object[] { "%selfly%" });
        User user1 = new User();
        user1.setUserType("1");
        List<User> users = jdbcDao.queryList(user1, criteria.include("userId", "userType", "loginName"));
        Assert.assertNotNull(users);
        for (User us : users) {
            Assert.assertNull(us.getEmail());
            Assert.assertEquals("1", us.getUserType());
            Assert.assertTrue(StrUtils.indexOf(us.getLoginName(), "selfly") != -1);
        }
    }

    @Test
    public void queryCount() {
        User u = new User();
        u.setUserType("1");
        int count = jdbcDao.queryCount(u);
        Assert.assertTrue(count > 0);
    }

    @Test
    public void queryCount2() {
        Criteria criteria = Criteria.select(User.class).where("userType", new Object[] { "1" });
        int count = jdbcDao.queryCount(criteria);
        Assert.assertTrue(count > 0);
    }

    @Test
    public void querySingleResult() {
        this.save();
        User u = new User();
        u.setUserId(-2L);
        u = jdbcDao.querySingleResult(u);
        Assert.assertNotNull(u);
        Assert.assertEquals(-2L, (long) u.getUserId());
    }

    @Test
    public void querySingleResult2() {
        this.save();
        Criteria criteria = Criteria.select(User.class).where("userId", new Object[] { -2L });
        User u = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(u);
        Assert.assertEquals(-2L, (long) u.getUserId());
    }

    @Test
    public void queryFunc() {
        Criteria criteria = Criteria.select(User.class).where("{length([loginName])}", ">", new Object[] { 8 });
        List<User> userList = jdbcDao.queryList(criteria);
        Assert.assertNotNull(userList);
        for (User u : userList) {
            Assert.assertTrue(u.getLoginName().length() > 8);
        }
    }

    @Test
    public void queryObject() {
        Criteria criteria = Criteria.select(User.class).addSelectFunc("max([userId])");
        Long userId = jdbcDao.queryObject(criteria);
        Assert.assertTrue(userId > 0);
    }

    @Test
    public void queryObject2() {
        Criteria criteria = Criteria.select(User.class)
            .addSelectFunc("length([loginName]) loginNameLength", false, true).where("loginName", "is not", null)
            .and("loginName", "!=", new Object[] { "" });
        List<Map<String, Object>> mapList = jdbcDao.queryRowMapList(criteria);
        Assert.assertNotNull(mapList);
        for (Map<String, Object> map : mapList) {
            Assert.assertTrue(map.get("loginNameLength") != null);
            Assert.assertTrue(map.get("userId") != null);
        }
    }

    @Test
    public void queryRowMap() {
        this.save();
        Criteria criteria = Criteria.select(User.class).where("userId", new Object[] { -2L });
        Map<String, Object> map = jdbcDao.queryRowMap(criteria);
        Assert.assertNotNull(map);
        Assert.assertTrue("selfly-2".equals(map.get("loginName")));
    }

    @Test
    public void queryRowMapList() {
        this.save();
        Criteria criteria = Criteria.select(User.class).addSelectFunc("distinct [loginName]")
            .where("loginName", "is not", null).and("loginName", "!=", new Object[] { "" });
        List<Map<String, Object>> mapList = jdbcDao.queryRowMapList(criteria);
        Assert.assertNotNull(mapList);
        for (Map<String, Object> map : mapList) {
            Assert.assertTrue(map.get("loginName") != null);
        }
    }

    @Test
    public void queryObjectForSql() {
        this.save();
        Long userId = (Long) jdbcDao.queryObjectForSql("select max(user_id) from USER");
        Assert.assertNotNull(userId);
    }

    @Test
    public void queryObjectForSql2() {
        this.save();
        this.save2();
        Long userId = (Long) jdbcDao.queryObjectForSql("select max(user_id) from USER where user_id < ?",
            new Object[] { 0L });
        Assert.assertTrue(userId < 0);
    }

    @Test
    public void querySingleResultForSql() {
        this.save();
        Map<String, Object> map = jdbcDao.querySingleResultForSql("select * from USER where user_id = -2");
        Assert.assertTrue(map.get("userId").equals(-2L));
    }

    @Test
    public void querySingleResultForSql2() {
        this.save();
        User user = jdbcDao.querySingleResultForSql("select * from USER where user_id = -2", User.class);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserId().equals(-2L));
    }

    @Test
    public void querySingleResultForSql3() {
        this.save();
        Map<String, Object> map = jdbcDao.querySingleResultForSql("select * from USER where user_id = ?",
            new Object[] { -2L });
        Assert.assertTrue(map.get("userId").equals(-2L));
    }

    @Test
    public void querySingleResultForSql4() {
        this.save();
        User user = jdbcDao.querySingleResultForSql("select * from USER where user_id = ?", new Object[] { -2 },
            User.class);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserId().equals(-2L));
    }

    @Test
    public void testBracket() {

        this.save();
        Criteria criteria = Criteria.select(User.class).where("userType", new Object[] { "1" }).begin()
            .and("loginName", new Object[] { "javaer@live.com" }).or("email", new Object[] { "javaer@live.com" }).end()
            .and("password", new Object[] { "123456" });
        User user = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(user);
        Assert.assertTrue("javaer@live.com".equals(user.getLoginName()) || "javaer@live.com".equals(user.getEmail()));

        criteria = Criteria.select(User.class).where("userType", new Object[] { "1" }).begin()
            .and("loginName", new Object[] { "selfly-2" }).or("email", new Object[] { "selfly-2" }).end()
            .and("password", new Object[] { "123456" });
        user = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(user);
        Assert.assertTrue("selfly-2".equals(user.getLoginName()) || "selfly-2".equals(user.getEmail()));
    }

    @Test
    public void testSelectSql() {

        this.save();
        List<Map<String, Object>> list = jdbcDao.queryListForSql("select * from USER where login_name = ?",
            new Object[] { "selfly-2" });
        for (Map<String, Object> map : list) {
            Assert.assertTrue("selfly-2".equals(map.get("loginName")));
        }
    }

    @Test
    public void testSelectSql2() {

        this.save();
        List<Map<String, Object>> list = jdbcDao.queryListForSql("select * from USER where login_name = 'selfly-2'");
        for (Map<String, Object> map : list) {
            Assert.assertTrue("selfly-2".equals(map.get("loginName")));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSelectSql3() {

        PageControl.performPage(1, 10);
        jdbcDao
            .queryListForSql("select t.* ,t2.login_name lgName from USER t left join USER t2 on t.user_id=t2.user_id");
        Pager pager = PageControl.getPager();
        List<Map<String, Object>> list = (List<Map<String, Object>>) pager.getList();
        Assert.assertTrue(list.size() == 10);
    }

    @Test
    public void testUpdateSql() {
        this.save();
        int i = jdbcDao.updateForSql("update USER set login_name = ? where user_id = ?", new Object[] { "aaaa", -2L });
        Assert.assertTrue(i == 1);

        User user = jdbcDao.get(User.class, -2L);
        Assert.assertEquals("aaaa", user.getLoginName());
    }

    @Test
    public void testAnnotationInsert() {

        //注解对应了 USER_A 表
        AnnotationUser annotationUser = new AnnotationUser();
        annotationUser.setLoginName("annotation_12");
        //数据库中desc是关键字 注解名称
        annotationUser.setDesc("test desc");
        annotationUser.setGmtCreate(new Date());
        Long userId = jdbcDao.insert(annotationUser);
        Assert.assertNotNull(userId);

        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, userId);
        Assert.assertEquals(annotationUser1.getLoginName(), "annotation_12");
    }

    @Test
    public void testAnnotationInsert2() {

        //注解对应了 USER_A 表 注意criteria中desc的处理
        Criteria criteria = Criteria.insert(AnnotationUser.class).into("loginName", "annUser")
            .into("`desc`", "test desc").into("gmtCreate", new Date());
        Long userId = jdbcDao.insert(criteria);
        Assert.assertNotNull(userId);

        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, userId);
        Assert.assertEquals(annotationUser1.getLoginName(), "annUser");
    }

    @Test
    public void testAnnotationSave() {

        jdbcDao.delete(AnnotationUser.class, -1L);
        //注解对应了 USER_A 表
        AnnotationUser annotationUser = new AnnotationUser();
        annotationUser.setUserId(-1L);
        annotationUser.setLoginName("annotation_12");
        //数据库中desc是关键字 注解名称
        annotationUser.setDesc("test desc");
        annotationUser.setGmtCreate(new Date());
        jdbcDao.save(annotationUser);

        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, -1L);
        Assert.assertNotNull(annotationUser1);
    }

    @Test
    public void testAnnotationUpdate() {

        Long userId = this.insertAnnotationUser();
        //注解对应了 USER_A 表
        AnnotationUser updateUser = new AnnotationUser();
        updateUser.setUserId(userId);
        updateUser.setLoginName("annotation_update");
        //数据库中desc是关键字 注解名称
        updateUser.setDesc("test desc update");
        int i = jdbcDao.update(updateUser);
        Assert.assertTrue(i > 0);

        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, userId);
        Assert.assertEquals(annotationUser1.getLoginName(), "annotation_update");
    }

    @Test
    public void testAnnotationUpdate2() {

        Long userId = this.insertAnnotationUser();

        Criteria criteria = Criteria.update(AnnotationUser.class).set("loginName", "criteriaUserUpdate")
            .where("userId", new Object[] { userId });

        int i = jdbcDao.update(criteria);
        Assert.assertTrue(i > 0);

        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, userId);
        Assert.assertEquals(annotationUser1.getLoginName(), "criteriaUserUpdate");
    }

    @Test
    public void testAnnotationDelete() {
        Long userId = this.insertAnnotationUser();
        AnnotationUser annotationUser = new AnnotationUser();
        annotationUser.setUserId(userId);
        int i = jdbcDao.delete(annotationUser);
        Assert.assertTrue(i > 0);
        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, userId);
        Assert.assertNull(annotationUser1);
    }

    @Test
    public void testAnnotationDelete2() {
        Long userId = this.insertAnnotationUser();
        Criteria criteria = Criteria.delete(AnnotationUser.class).where("userId", new Object[] { userId });

        int i = jdbcDao.delete(criteria);
        Assert.assertTrue(i > 0);
        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, userId);
        Assert.assertNull(annotationUser1);
    }

    @Test
    public void testAnnotationDelete3() {
        Long userId = this.insertAnnotationUser();
        int i = jdbcDao.delete(AnnotationUser.class, userId);
        Assert.assertTrue(i > 0);
        //查询时表中没有gmtCreate字段，注解进行了忽略
        AnnotationUser annotationUser1 = jdbcDao.get(AnnotationUser.class, userId);
        Assert.assertNull(annotationUser1);
    }

    @Test
    public void testAnnotationSelect() {
        this.insertAnnotationUser();
        this.insertAnnotationUser();
        List<AnnotationUser> userList = jdbcDao.queryList(AnnotationUser.class);
        Assert.assertTrue(userList.size() > 0);
    }

    @Test
    public void testAnnotationSelect2() {
        this.insertAnnotationUser();
        Long userId = this.insertAnnotationUser();
        AnnotationUser annotationUser = new AnnotationUser();
        annotationUser.setUserId(userId);
        List<AnnotationUser> userList = jdbcDao.queryList(annotationUser);
        Assert.assertTrue(userList.size() == 1);
    }

    @Test
    public void testAnnotationSelect3() {
        this.insertAnnotationUser();
        this.insertAnnotationUser();
        Criteria criteria = Criteria.select(AnnotationUser.class).exclude("userId");
        List<AnnotationUser> userList = jdbcDao.queryList(criteria);
        Assert.assertTrue(userList.size() > 1);
        for (AnnotationUser annotationUser : userList) {
            Assert.assertNull(annotationUser.getUserId());
        }
    }

    private Long insertAnnotationUser() {

        //注解对应了 USER_A 表
        AnnotationUser annotationUser = new AnnotationUser();
        annotationUser.setLoginName("annotation_12");
        annotationUser.setDesc("test desc");
        annotationUser.setGmtCreate(new Date());
        return jdbcDao.insert(annotationUser);
    }
}
