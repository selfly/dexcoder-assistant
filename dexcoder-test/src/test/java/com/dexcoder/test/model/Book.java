package com.dexcoder.test.model;

import java.util.Date;

import com.dexcoder.commons.pager.Pageable;

/**
 * Created by liyd on 2015-10-27.
 */
public class Book extends Pageable {

    private static final long serialVersionUID = 6325757555273943337L;

    private Long              bookId;

    private String            bookName;

    private Date              gmtCreate;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
