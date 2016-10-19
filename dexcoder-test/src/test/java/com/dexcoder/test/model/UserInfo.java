package com.dexcoder.test.model;

import java.util.Date;

import com.dexcoder.commons.pager.Pageable;

/**
 * Created by liyd on 16/10/17.
 */
public class UserInfo extends Pageable {

    private Long    userInfoId;

    private String  userName;

    private String  password;

    private String  email;

    private Integer age;

    private Date    gmtCreate;

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
