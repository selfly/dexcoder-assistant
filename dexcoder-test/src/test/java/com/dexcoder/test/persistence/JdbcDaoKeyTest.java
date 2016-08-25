package com.dexcoder.test.persistence;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dexcoder.dal.JdbcDao;
import com.dexcoder.test.model.People;

/**
 * Created by liyd on 16/8/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:key-applicationContext.xml" })
public class JdbcDaoKeyTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void insert() {

        for (int i = 0; i < 10; i++) {
            People people = new People();
            people.setPeopleName("liyd" + i);
            people.setPeopleAge(i);
            people.setGmtCreate(new Date());

            jdbcDao.insert(people);
        }
    }

    @Test
    public void get() {

        People people = new People();
        people.setPeopleName("liyd-get");
        people.setPeopleAge(12);
        people.setGmtCreate(new Date());
        String id = jdbcDao.insert(people);

        People people1 = jdbcDao.get(People.class, id);
        Assert.assertEquals(people.getPeopleName(), people1.getPeopleName());

    }

}
