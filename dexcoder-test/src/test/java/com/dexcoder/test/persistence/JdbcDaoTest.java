package com.dexcoder.test.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dexcoder.commons.pager.Pager;
import com.dexcoder.jdbc.JdbcDao;
import com.dexcoder.jdbc.build.Criteria;
import com.dexcoder.jdbc.spring.datasource.DynamicDataSource;
import com.dexcoder.jdbc.spring.page.PageControl;
import com.dexcoder.test.BaseTest;
import com.dexcoder.test.model.User;

/**
 * Created by liyd on 3/3/15.
 */
public class JdbcDaoTest extends BaseTest {

    @Autowired
    private JdbcDao           jdbcDao;

    //    @Autowired
    private DynamicDataSource dynamicDataSource;

    @BeforeClass
    public static void before() {

    }

    //    @Test
    //    public void insertData() {
    //        //删除所有数据并插入50条测试数据
    //        jdbcDao.deleteAll(User.class);
    //        for (int i = 1; i < 51; i++) {
    //            User user = new User();
    //            user.setLoginName("selfly" + i);
    //            user.setPassword("123456");
    //            user.setEmail("javaer" + i + "@live.com");
    //            user.setUserAge(i);
    //            user.setGmtCreate(new Date());
    //            Long id = jdbcDao.insert(user);
    //            System.out.println("insert:" + id);
    //        }
    //    }

    @Test
    public void insert() {
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setLoginName("selfly_a" + i);
            user.setPassword("123456");
            user.setEmail("javaer@live.com");
            user.setUserAge(18);
            user.setUserType("1");
            user.setGmtCreate(new Date());
            Long id = jdbcDao.insert(user);
            System.out.println("insert:" + id);
        }

    }

    @Test
    public void insert2() {
        Criteria criteria = Criteria.insert(User.class).set("loginName", "selfly_b").set("password", "12345678")
            .set("email", "selflly@foxmail.com").set("userAge", 22).set("userType", "2").set("gmtCreate", new Date());
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
        user.setUserType("1");
        user.setGmtCreate(new Date());
        jdbcDao.save(user);
    }

    @Test
    public void save2() {
        Criteria criteria = Criteria.insert(User.class).set("userId", -2L).set("loginName", "selfly-2")
            .set("password", "12345678").set("email", "selflly@foxmail.com").set("userAge", 22).set("userType", "2")
            .set("gmtCreate", new Date());
        jdbcDao.save(criteria);
    }

    @Test
    public void update() {
        User user = new User();
        user.setUserId(57L);
        user.setPassword("abcdef");
        user.setGmtModify(new Date());
        jdbcDao.update(user);
    }

    @Test
    public void update2() {
        Criteria criteria = Criteria.update(User.class).set("password", "update222")
            .where("userId", new Object[] { 56L, -1L, -2L });
        jdbcDao.update(criteria);
    }

    @Test
    public void delete() {
        User u = new User();
        u.setLoginName("selfly-1");
        u.setUserType("1");
        jdbcDao.delete(u);
    }

    @Test
    public void delete2() {
        jdbcDao.delete(Criteria.delete(User.class).where("userId", "in", new Object[] { 56L, -2L }));
    }

    @Test
    public void delete3() {
        jdbcDao.delete(User.class, 57L);
    }

    @Test
    public void queryList() {
        User u = new User();
        u.setUserType("1");
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
        Criteria criteria = Criteria.select(User.class).include("loginName", "userId").asc("userId");
        jdbcDao.queryList(criteria);
        Pager pager = PageControl.getPager();
        List<User> users = pager.getList(User.class);
        Assert.assertNotNull(users);
        System.out.println(pager.getItemsTotal());
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList3() {
        Criteria criteria = Criteria.select(User.class).exclude("userId").where("userType", new Object[] { "1" })
            .asc("userAge").desc("userId");
        List<User> users = jdbcDao.queryList(criteria);
        Assert.assertNotNull(users);
        for (User us : users) {
            System.out.println(us.getUserId() + " " + us.getLoginName() + " " + us.getUserType());
        }
    }

    @Test
    public void queryList4() {
        Criteria criteria = Criteria.select(User.class).where("loginName", "like", new Object[] { "%selfly%" });
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
        u.setLoginName("selfly_a91");
        int count = jdbcDao.queryCount(u);
        System.out.println(count);
    }

    @Test
    public void queryCount2() {
        Criteria criteria = Criteria.select(User.class).where("loginName", new Object[] { "selfly_a92" })
            .or("userAge", new Object[] { 27 });
        int count = jdbcDao.queryCount(criteria);
        System.out.println(count);
    }

    @Test
    public void get() {

        User u = jdbcDao.get(User.class, 63L);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());

    }

    @Test
    public void get2() {
        Criteria criteria = Criteria.select(User.class).include("loginName");
        User u = jdbcDao.get(criteria, 73L);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void querySingleResult() {
        User u = new User();
        u.setLoginName("selfly_a94");
        u.setUserType("1");
        u = jdbcDao.querySingleResult(u);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void querySingleResult2() {
        Criteria criteria = Criteria.select(User.class).where("loginName", new Object[] { "selfly_a94" })
            .and("userType", new Object[] { "1" });
        User u = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " " + u.getLoginName() + " " + u.getUserType());
    }

    @Test
    public void testBracket() {

        Criteria criteria = Criteria.select(User.class).where("userType", new Object[] { "1" }).begin()
            .and("loginName", new Object[] { "selfly_a94" }).or("email", new Object[] { "javaer@live.com" }).end()
            .and("password", new Object[] { "123456" });
        User user = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(user);
        System.out.println(user.getLoginName());
    }

    @Test
    public void testSelectSql() {

        List<Map<String, Object>> list = jdbcDao.queryForSql("select * from USER where login_name = ?",
            new Object[] { "selfly_a99" });
        for (Map<String, Object> map : list) {
            System.out.println(map.get("user_id"));
            System.out.println(map.get("login_name"));
        }
    }

    @Test
    public void testSelectSql2() {

        List<Map<String, Object>> list = jdbcDao.queryForSql("select * from USER where login_name = 'selfly_a99'");
        for (Map<String, Object> map : list) {
            System.out.println(map.get("user_id"));
            System.out.println(map.get("login_name"));
        }
    }

    @Test
    public void testUpdateSql() {
        int i = jdbcDao.updateForSql("update USER set login_name = ? where user_id = ?", new Object[] { "aaaa", 152L });
        System.out.println(i);
    }

    @Test
    public void testBatisSql() {
        User user = new User();
        user.setLoginName("selfly_a93");
        List<Map<String, Object>> mapList = jdbcDao.queryForSql("User.getUser", "user", new Object[] { user, user });
        for (Map<String, Object> map : mapList) {
            System.out.println(map.get("user_id"));
            System.out.println(map.get("login_name"));
        }
    }

    //    @Test
    //    public void multiTableBook() {
    //        for (int i = 1; i < 51; i++) {
    //            Book book = new Book();
    //            book.setBookId((long) i);
    //            book.setBookName("测试book" + i);
    //            book.setGmtCreate(new Date());
    //            jdbcDao.save(book);
    //        }
    //        System.out.println("=================");
    //    }

    //    @Test
    //    public void multiTableChapter() {
    //        for (int i = 1; i < 51; i++) {
    //            Chapter chapter = new Chapter();
    //            chapter.setChapterId((long) i);
    //            chapter.setBookId(5L);
    //            chapter.setChapterName("章节一" + i);
    //            chapter.setGmtCreate(new Date());
    //            jdbcDao.save(chapter);
    //        }
    //        System.out.println("=================");
    //        for (int i = 51; i < 101; i++) {
    //            Chapter chapter = new Chapter();
    //            chapter.setChapterId((long) i);
    //            chapter.setBookId(6L);
    //            chapter.setChapterName("章节二" + i);
    //            chapter.setGmtCreate(new Date());
    //            jdbcDao.save(chapter);
    //        }
    //        System.out.println("=================");
    //    }
    //
    //    @Test
    //    public void multiTableChapterQuery() {
    //        Chapter chapter = new Chapter();
    //        chapter.setChapterId(22L);
    //        chapter.setBookId(5L);
    //        chapter = jdbcDao.querySingleResult(chapter);
    //        System.out.println(chapter.getChapterName());
    //        chapter = jdbcDao.querySingleResult(Criteria.create(Chapter.class)
    //                .where("chapterId", new Object[]{67L}).and("bookId", new Object[]{6L}));
    //        System.out.println(chapter.getChapterName());
    //    }
    //
    //    @Test
    //    public void multiTableChapterUpdate() {
    //        Chapter chapter = new Chapter();
    //        chapter.setChapterId(22L);
    //        chapter.setBookId(5L);
    //        chapter.setChapterName("更新后章节名");
    //        jdbcDao.update(chapter);
    //
    //        Chapter tmp = jdbcDao.querySingleResult(Criteria.create(Chapter.class)
    //                .where("chapterId", new Object[]{22L}).and("bookId", new Object[]{5L}));
    //        System.out.println(tmp.getChapterName());
    //    }
    //
    //    @Test
    //    public void multiTableChapterDelete() {
    //        Chapter chapter = new Chapter();
    //        chapter.setChapterId(23L);
    //        chapter.setBookId(5L);
    //        jdbcDao.delete(chapter);
    //
    //        Chapter tmp = jdbcDao.querySingleResult(Criteria.create(Chapter.class)
    //                .where("chapterId", new Object[]{23L}).and("bookId", new Object[]{5L}));
    //        Assert.assertNull(tmp);
    //    }
    //
    //    @Test
    //    public void dyDsInsert() {
    //        User user = new User();
    //        user.setLoginName("selfly");
    //        user.setGmtCreate(new Date());
    //        Long id = jdbcDao.insert(user);
    //
    //        User u = jdbcDao.get(Criteria.create(User.class)
    //                .include("userId", "loginName", "gmtCreate"), id);
    //        Assert.assertNotNull(u);
    //        System.out.println(u.getUserId() + " : " + u.getLoginName());
    //    }
    //
    //    @Test
    //    public void dyDsGet() {
    //        User u = jdbcDao.get(Criteria.create(User.class)
    //                .include("userId", "loginName", "gmtCreate"), 6L);
    //        Assert.assertNull(u);
    //    }
    //
    //    @Test
    //    public void dyDsGet2() {
    //        List<Map<String, String>> dsList = new ArrayList<Map<String, String>>();
    //
    //        Map<String, String> map = new HashMap<String, String>();
    //        map.put("id", "dataSource4");
    //        map.put("class", "org.apache.commons.dbcp.BasicDataSource");
    //        map.put("default", "true");
    //        map.put("weight", "10");
    //        map.put("mode", "rw");
    //        map.put("driverClassName", "com.mysql.jdbc.Driver");
    //        map.put("url",
    //                "jdbc:mysql://localhost:3306/db1?useUnicode=true&amp;characterEncoding=utf-8");
    //        map.put("username", "root");
    //        map.put("password", "");
    //        dsList.add(map);
    //
    //        int i = 0;
    //        while (i < 100) {
    //            User u = jdbcDao.get(
    //                    Criteria.create(User.class).include("userId", "loginName", "gmtCreate"), 6L);
    //            System.out.println(u == null ? "null" : u.getLoginName());
    //
    //            if (i == 70) {
    //                dynamicDataSource.initDataSources(dsList);
    //            }
    //            i++;
    //        }
    //    }
    //
    //    @Test
    //    public void testSql() {
    //        User user = new User();
    //        user.setLoginName("selfly38");
    //        user.setUserId(11L);
    //        jdbcDao.queryForSql("getUser", "user", user);
    //    }
    //
    //    @Test
    //    public void testSql2() {
    //        Map<String, Object> map = new HashMap<String, Object>();
    //        List<String> list = new ArrayList<String>();
    //        list.add("selfly");
    //        list.add("selfly37");
    //        list.add("selfly38");
    //
    //        map.put("list", list);
    //
    //        User user = new User();
    //        user.setLoginName("selfly39");
    //        user.setUserId(11L);
    //
    //        map.put("user", user);
    //
    //        jdbcDao.queryForSql("getUser2", map);
    //    }
    //
    //
    //    @Test
    //    public void testSql3() {
    ////        List<String> list = new ArrayList<String>();
    ////        list.add("selfly37");
    ////        list.add("selfly38");
    ////        List<Map<String, Object>> result = jdbcDao.queryForSql("queryUserList2", "list", list);
    ////        System.out.println(result.size());
    ////        System.out.println(result.iterator().next().get("login_name"));
    ////        System.out.println("===========");
    //    }
}
