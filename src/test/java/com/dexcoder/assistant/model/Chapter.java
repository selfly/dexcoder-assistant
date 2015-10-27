package com.dexcoder.assistant.model;

import java.util.Date;

import com.dexcoder.assistant.pager.Pageable;

/**
 * Created by liyd on 2015-10-27.
 */
public class Chapter extends Pageable {

    private static final long serialVersionUID = 4281180331848977421L;

    private Long              chapterId;

    private Long              bookId;

    private String            chapterName;

    private Date              gmtCreate;

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
