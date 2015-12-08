package com.dexcoder.jdbc.test;

import com.dexcoder.jdbc.BoundSql;
import com.dexcoder.jdbc.DefaultNameHandler;
import com.dexcoder.jdbc.build.Criteria;
import com.dexcoder.jdbc.test.model.User;
import org.junit.Test;

/**
 * Created by liyd on 2015-12-8.
 */
public class CriteriaTest {


    @Test
    public void insertSql() {
        User user = new User();
        user.setEmail("selfly@dexcoder.com");
        user.setUserAge(18);
        user.setUserType("1");

        Criteria criteria = Criteria.insert(User.class).into("userId", 10000L).into("loginName", "selfly").into("password", "123456");
        BoundSql boundSql = criteria.build(user, true, new DefaultNameHandler());
        System.out.println(boundSql.getSql());
        for (Object obj : boundSql.getParameters()) {
            System.out.println(obj);
        }
    }

    @Test
    public void updateSql() {
        User user = new User();
        user.setEmail("selfly@dexcoder.com");
        user.setUserAge(18);
        user.setUserType("1");

        Criteria criteria = Criteria.update(User.class).set("loginName", "selfly").set("password", "123456").where("userId", "not in", new Object[]{10000L, 100001L, 10000L});
        BoundSql boundSql = criteria.build(user, true, new DefaultNameHandler());
        System.out.println(boundSql.getSql());
        for (Object obj : boundSql.getParameters()) {
            System.out.println(obj);
        }
    }

    @Test
    public void deleteSql() {
        User user = new User();
        user.setEmail("selfly@dexcoder.com");
        user.setUserAge(18);
//        user.setUserId(10000L);
//        user.setUserType("1");

        Criteria criteria = Criteria.delete(User.class).where("userId", "not in", new Object[]{10000L, 100001L, 10000L}).begin().and("userType", new Object[]{1111}).or("password", new Object[]{"123456"}).end();
        BoundSql boundSql = criteria.build(user, true, new DefaultNameHandler());
        System.out.println(boundSql.getSql());
        for (Object obj : boundSql.getParameters()) {
            System.out.println(obj);
        }
    }

    @Test
    public void selectSql() {

        User user = new User();
        user.setEmail("selfly@dexcoder.com");
        user.setUserAge(18);

        BoundSql boundSql = Criteria.select(User.class).include("loginName").where("userId", new Object[]{111, 333}).build(user, true, new DefaultNameHandler());
        System.out.println(boundSql.getSql());
        for (Object obj : boundSql.getParameters()) {
            System.out.println(obj);
        }
    }

    @Test
    public void nativeUpdateSql() {

        User user = new User();
        user.setEmail("selfly@dexcoder.com");
        user.setUserType("1");

        Criteria criteria = Criteria.update(User.class).set("[userAge]", "[userAge] + 1").set("password", "123456").where("userId", "not in", new Object[]{10000L, 100001L, 10000L});
        BoundSql boundSql = criteria.build(user, true, new DefaultNameHandler());
        System.out.println(boundSql.getSql());
        for (Object obj : boundSql.getParameters()) {
            System.out.println(obj);
        }
    }

    @Test
    public void nativeSelectSql() {

        User user = new User();
        user.setEmail("selfly@dexcoder.com");
        user.setUserType("1");

        Criteria criteria = Criteria.select(User.class).include("userId").where("[userId]", new Object[]{"[userAge]"});
        BoundSql boundSql = criteria.build(user, true, new DefaultNameHandler());
        System.out.println(boundSql.getSql());
        for (Object obj : boundSql.getParameters()) {
            System.out.println(obj);
        }
    }

    @Test
    public void nativeDeleteSql() {

        User user = new User();
        user.setEmail("selfly@dexcoder.com");
        user.setUserType("1");

        Criteria criteria = Criteria.delete(User.class).where("[userId]", new Object[]{"[userAge]"});
        BoundSql boundSql = criteria.build(user, true, new DefaultNameHandler());
        System.out.println(boundSql.getSql());
        for (Object obj : boundSql.getParameters()) {
            System.out.println(obj);
        }
    }

}
