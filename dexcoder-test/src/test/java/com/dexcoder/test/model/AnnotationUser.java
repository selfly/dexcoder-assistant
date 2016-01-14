package com.dexcoder.test.model;

import java.util.Date;

import com.dexcoder.commons.pager.Pageable;
import com.dexcoder.dal.annotation.Column;
import com.dexcoder.dal.annotation.Table;
import com.dexcoder.dal.annotation.Transient;

/**
 * Created by liyd on 2016-1-14.
 */
@Table(name = "USER_A", pkField = "userId", pkColumn = "USER_ID")
public class AnnotationUser extends Pageable {

    private static final long serialVersionUID = -3902415084403784275L;

    /** 用户id */
    private Long              userId;

    /** 登录名 */
    private String            loginName;

    /** 注释 数据库关键字 */
    private String            desc;

    /** 创建时间 */
    private Date              gmtCreate;

    /** 修改时间 数据库无 */
    private Date              gmtModify;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "`DESC`")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Transient
    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

}
