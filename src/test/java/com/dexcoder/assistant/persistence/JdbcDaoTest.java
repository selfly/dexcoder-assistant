package com.dexcoder.assistant.persistence;

import java.util.*;

import com.dexcoder.assistant.model.Book;
import com.dexcoder.assistant.model.Chapter;
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
    private JdbcDao           jdbcDao;

    //    @Autowired
    private DynamicDataSource dynamicDataSource;

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
        //        Criteria criteria = Criteria.create(User.class)
        //            .where("loginName", new Object[] { "selfly13" }).or("userAge", new Object[] { 33 });
        //        jdbcDao.delete(criteria);

        jdbcDao
            .delete(Criteria.create(User.class).where("userId", "in", new Object[]{88L, 99L}));
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
            .where("userType", new Object[]{UserType.MEMBER.getCode()}).asc("userAge")
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
            .or("email", new Object[]{"javaer1@live.com"}).endBracket()
            .and("password", new Object[] { "123456" });
        User user = jdbcDao.querySingleResult(criteria);
        Assert.assertNotNull(user);
        System.out.println(user.getLoginName());
    }

    @Test
    public void multiTableBook() {
        for (int i = 1; i < 51; i++) {
            Book book = new Book();
            book.setBookId((long) i);
            book.setBookName("测试book" + i);
            book.setGmtCreate(new Date());
            jdbcDao.save(book);
        }
        System.out.println("=================");
    }

    @Test
    public void multiTableChapter() {
        for (int i = 1; i < 51; i++) {
            Chapter chapter = new Chapter();
            chapter.setChapterId((long) i);
            chapter.setBookId(5L);
            chapter.setChapterName("章节一" + i);
            chapter.setGmtCreate(new Date());
            jdbcDao.save(chapter);
        }
        System.out.println("=================");
        for (int i = 51; i < 101; i++) {
            Chapter chapter = new Chapter();
            chapter.setChapterId((long) i);
            chapter.setBookId(6L);
            chapter.setChapterName("章节二" + i);
            chapter.setGmtCreate(new Date());
            jdbcDao.save(chapter);
        }
        System.out.println("=================");
    }

    @Test
    public void multiTableChapterQuery() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(22L);
        chapter.setBookId(5L);
        chapter = jdbcDao.querySingleResult(chapter);
        System.out.println(chapter.getChapterName());
        chapter = jdbcDao.querySingleResult(Criteria.create(Chapter.class)
            .where("chapterId", new Object[] { 67L }).and("bookId", new Object[] { 6L }));
        System.out.println(chapter.getChapterName());
    }

    @Test
    public void multiTableChapterUpdate() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(22L);
        chapter.setBookId(5L);
        chapter.setChapterName("更新后章节名");
        jdbcDao.update(chapter);

        Chapter tmp = jdbcDao.querySingleResult(Criteria.create(Chapter.class)
            .where("chapterId", new Object[] { 22L }).and("bookId", new Object[] { 5L }));
        System.out.println(tmp.getChapterName());
    }

    @Test
    public void multiTableChapterDelete() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(23L);
        chapter.setBookId(5L);
        jdbcDao.delete(chapter);

        Chapter tmp = jdbcDao.querySingleResult(Criteria.create(Chapter.class)
            .where("chapterId", new Object[] { 23L }).and("bookId", new Object[] { 5L }));
        Assert.assertNull(tmp);
    }

    @Test
    public void dyDsInsert() {
        User user = new User();
        user.setLoginName("selfly");
        user.setGmtCreate(new Date());
        Long id = jdbcDao.insert(user);

        User u = jdbcDao.get(Criteria.create(User.class)
                .include("userId", "loginName", "gmtCreate"), id);
        Assert.assertNotNull(u);
        System.out.println(u.getUserId() + " : " + u.getLoginName());
    }

    @Test
    public void dyDsGet() {
        User u = jdbcDao.get(Criteria.create(User.class)
            .include("userId", "loginName", "gmtCreate"), 6L);
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
        map.put("driverClassName", "com.mysql.jdbc.Driver");
        map.put("url",
            "jdbc:mysql://localhost:3306/db1?useUnicode=true&amp;characterEncoding=utf-8");
        map.put("username", "root");
        map.put("password", "");
        dsList.add(map);

        int i = 0;
        while (i < 100) {
            User u = jdbcDao.get(
                Criteria.create(User.class).include("userId", "loginName", "gmtCreate"), 6L);
            System.out.println(u == null ? "null" : u.getLoginName());

            if (i == 70) {
                dynamicDataSource.initDataSources(dsList);
            }
            i++;
        }
    }

    @Test
    public void testSql() {
//        User user = new User();
//        user.setLoginName("selfly38");
//        List<Map<String, Object>> result = jdbcDao.queryForSql("queryUserList", "user", user);
//        System.out.println(result.size());
//        System.out.println(result.iterator().next().get("login_name"));
//        System.out.println("===========");
    }

    @Test
    public void testSql2() {
//        User user = new User();
//        user.setUserId(37L);
//        user.setLoginName("selfly37");
//        List<Map<String, Object>> result = jdbcDao.queryForSql("queryUserList", "user", user);
//        System.out.println(result.size());
//        System.out.println(result.iterator().next().get("login_name"));
//        System.out.println("===========");
    }


    @Test
    public void testSql3() {
//        List<String> list = new ArrayList<String>();
//        list.add("selfly37");
//        list.add("selfly38");
//        List<Map<String, Object>> result = jdbcDao.queryForSql("queryUserList2", "list", list);
//        System.out.println(result.size());
//        System.out.println(result.iterator().next().get("login_name"));
//        System.out.println("===========");
    }
}
