package com.dexcoder.test.model;

import java.util.Date;

import com.dexcoder.commons.page.Pageable;
import com.dexcoder.dal.annotation.Column;
import com.dexcoder.dal.annotation.Table;
import com.dexcoder.dal.annotation.Transient;

/**
 * Created by liyd on 2016-1-14.
 */
@Table(name = "USER_A", pkField = "usernameId")
public class AnnotationUser extends Pageable {

    private static final long serialVersionUID = -3902415084403784275L;

    /** 用户id */
    private Long              usernameId;

    /** 登录名 */
    private String            username;

    /** 注释 数据库关键字 */
    private String            description;

    /** 创建时间 */
    private Date              gmtCreate;

    /** 修改时间 数据库无 */
    private Date              gmtModify;

    @Column("user_id")
    public Long getUsernameId() {
        return usernameId;
    }

    public void setUsernameId(Long usernameId) {
        this.usernameId = usernameId;
    }

    @Column("`DESC`")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column("login_name")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
