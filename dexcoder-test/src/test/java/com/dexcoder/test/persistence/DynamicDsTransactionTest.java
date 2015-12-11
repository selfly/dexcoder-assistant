//package com.dexcoder.test.persistence;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.dexcoder.jdbc.JdbcDao;
//import com.dexcoder.test.BaseTest;
//import com.dexcoder.test.transaction.JdbcDaoTransactionService;
//
///**
// * Created by liyd on 2015-11-6.
// */
//public class DynamicDsTransactionTest extends BaseTest {
//
//    @Autowired
//    private JdbcDao                   jdbcDao;
//
//    @Autowired
////    private JdbcDaoTransactionService jdbcDaoTransactionService;
//
//    @Test
//    public void testTrans() {
//
//        //插入测试数据
//        List<Long> bookIdList = new ArrayList<Long>();
//        for (int i = 0; i < 20; i++) {
//            Book book = new Book();
//            book.setBookName("book" + i);
//            book.setGmtCreate(new Date());
//            Long bookId = jdbcDao.insert(book);
//            bookIdList.add(bookId);
//        }
//        for (Long bookId : bookIdList) {
//            //此处捕获异常，以免程序终止
//            try {
//                jdbcDaoTransactionService.updateBook(bookId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//}
