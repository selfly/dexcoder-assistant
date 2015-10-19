package com.dexcoder.assistant.model;

import java.util.Date;

import com.dexcoder.assistant.enums.UserType;

/**
 * Created by liyd on 2015-6-4.
 */
public class UserVo {

    /** 用户id */
    private Long     userId;

    /** 登录名 */
    private String   loginName;

    /** 密码 */
    private String   password;

    /** 年龄 */
    private Integer  userAge;

    /** 用户类型 */
    private UserType userType;

    /** 邮箱 */
    private String   email;

    /** 创建时间 */
    private Date     gmtCreate;

    /** 修改时间 */
    private Date     gmtModify;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
