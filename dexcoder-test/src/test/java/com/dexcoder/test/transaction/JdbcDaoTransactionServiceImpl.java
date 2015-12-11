//package com.dexcoder.test.transaction;
//
//import java.util.Date;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.dexcoder.jdbc.JdbcDao;
//import com.dexcoder.jdbc.test.model.Book;
//import com.dexcoder.jdbc.test.model.Chapter;
//
///**
// * Created by liyd on 2015-11-6.
// */
////@Service
//public class JdbcDaoTransactionServiceImpl implements JdbcDaoTransactionService {
//
//    private static final Logger LOG = LoggerFactory.getLogger(JdbcDaoTransactionServiceImpl.class);
//
//    @Autowired
//    private JdbcDao             jdbcDao;
//
//    public void updateBook(Long bookId) {
//
//        //先读操作,应该都是null，因为读库无数据
//        Book book = jdbcDao.get(Book.class, bookId);
//        LOG.info("book:{}", book);
//
//        //这里只是测试，数据未做主从同步，预料中首次读数据源无数据会返回null，后面会使用写数据源
//        if (book == null) {
//            book = new Book();
//            book.setBookName("nullBook");
//            //写库上有数据，所以这批数据应该多出来了，但是有一半会被回滚掉，而读库还是空的
//            Long id = jdbcDao.insert(book);
//            book.setBookId(id);
//        }
//
//        //写操作
//        Chapter chapter = new Chapter();
//        chapter.setBookId(bookId);
//        chapter.setChapterName("test chapter");
//        chapter.setGmtCreate(new Date());
//        jdbcDao.insert(chapter);
//
//        //写操作
//        book.setBookName("update book");
//        jdbcDao.update(book);
//
//        //抛出异常，看事务是否回滚
//        if (bookId % 2 == 0) {
//            throw new RuntimeException("事务回滚");
//        }
//
//    }
//}
