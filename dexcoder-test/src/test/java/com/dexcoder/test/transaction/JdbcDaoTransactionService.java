package com.dexcoder.test.transaction;

/**
 * JdbcDao事务测试
 * <p/>
 * Created by liyd on 2015-11-6.
 */
public interface JdbcDaoTransactionService {

    /**
     * jdbcDao 多数据源时事务测试
     *
     * @param bookId
     */
    void updateBook(Long bookId);
}
