package com.dexcoder.jdbc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 加载spring配置文件
 * 
 * User: liyd
 * Date: 2/13/14
 * Time: 5:33 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:src/test/resources/applicationContext.xml"})
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
//@Transactional()
public class BaseTest {

    @Test
    public void init() {
        System.out.println("init...");
    }
}
