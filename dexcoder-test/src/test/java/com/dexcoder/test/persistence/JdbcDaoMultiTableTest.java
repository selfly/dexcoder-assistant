package com.dexcoder.test.persistence;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dexcoder.dal.JdbcDao;
import com.dexcoder.dal.build.Criteria;
import com.dexcoder.test.model.Book;
import com.dexcoder.test.model.Chapter;

/**
 * 水平分表测试
 *
 * Created by liyd on 2016-1-14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:multiTable-applicationContext.xml" })
public class JdbcDaoMultiTableTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void multiTableBook() {
        //50本书，这里是否分表插入了需要人工看数据库
        //毕竟如果没有分表，get也没分表还是能取到结果
        for (int i = 1; i < 51; i++) {
            jdbcDao.delete(Book.class,(long)i);
            Book book = new Book();
            book.setBookId((long) i);
            book.setBookName("测试book" + i);
            book.setGmtCreate(new Date());
            jdbcDao.save(book);
        }
    }

    @Test
    public void multiTableChapter() {
        //两次插入bookId不同，根据CustomMappingHandler中的策略应该在不同的表中
        for (int i = 1; i < 51; i++) {

            Chapter chapter = new Chapter();
            chapter.setChapterId((long) i);
            chapter.setBookId(5L);
            jdbcDao.delete(chapter);

            chapter.setChapterName("章节一" + i);
            chapter.setGmtCreate(new Date());
            jdbcDao.save(chapter);
        }
        for (int i = 51; i < 101; i++) {
            Chapter chapter = new Chapter();
            chapter.setChapterId((long) i);
            chapter.setBookId(6L);
            jdbcDao.delete(chapter);

            chapter.setChapterName("章节二" + i);
            chapter.setGmtCreate(new Date());
            jdbcDao.save(chapter);
        }
    }

    @Test
    public void multiTableChapterQuery() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(22L);
        chapter.setBookId(5L);
        chapter = jdbcDao.querySingleResult(chapter);
        Assert.assertNotNull(chapter);
        chapter = jdbcDao.querySingleResult(Criteria.select(Chapter.class).where("chapterId", new Object[] { 67L })
            .and("bookId", new Object[] { 6L }));
        Assert.assertNotNull(chapter);
    }

    @Test
    public void multiTableChapterUpdate() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(22L);
        chapter.setBookId(5L);
        chapter.setChapterName("updateChapter");
        jdbcDao.update(chapter);

        Chapter tmp = jdbcDao.querySingleResult(Criteria.select(Chapter.class).where("chapterId", new Object[] { 22L })
            .and("bookId", new Object[] { 5L }));
        Assert.assertEquals("updateChapter", tmp.getChapterName());
    }

    @Test
    public void multiTableChapterDelete() {
        Chapter chapter = new Chapter();
        chapter.setChapterId(23L);
        chapter.setBookId(5L);
        jdbcDao.delete(chapter);

        Chapter tmp = jdbcDao.querySingleResult(Criteria.select(Chapter.class).where("chapterId", new Object[] { 23L })
            .and("bookId", new Object[] { 5L }));
        Assert.assertNull(tmp);
    }

}
