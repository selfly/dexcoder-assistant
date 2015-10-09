package com.dexcoder.assistant.persistence;

import java.util.Date;

import com.dexcoder.assistant.pager.Pageable;

/**
 * 用户
 *
 * User: liyd
 * Date: Wed Dec 24 16:46:48 CST 2014
 */
public class User extends Pageable {

    private static final long serialVersionUID = 8166785520231287816L;

    /** 用户id */
    private Long              userId;

    /** 登录名 */
    private String            loginName;

    /** 密码 */
    private String            password;

    /** 用户类型 */
    private String            userType;

    /** 状态 */
    private String            status;

    /** 姓名 */
    private String            username;

    /** 邮箱 */
    private String            email;

    /** 头像图片 */
    private String            avatar;

    /** 管理密码 */
    private String            adminPassword;

    /** 性別 1男 0女 */
    private String            sex;

    /** 积分 */
    private Integer           score;

    /** 不可用积分 */
    private Integer           disabledScore;

    /** 威望 */
    private Integer           prestige;

    /** 总被赞数 */
    private Integer           totalUp;

    /** 最后登录时间 */
    private Date              gmtLastLogin;

    /** 创建时间 */
    private Date              gmtCreate;

    /** 修改时间 */
    private Date              gmtModify;

    /** 创建人 */
    private String            creator;

    /** 编辑时间 */
    private String            modifier;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getTotalUp() {
        return totalUp;
    }

    public void setTotalUp(Integer totalUp) {
        this.totalUp = totalUp;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getDisabledScore() {
        return disabledScore;
    }

    public void setDisabledScore(Integer disabledScore) {
        this.disabledScore = disabledScore;
    }

    public Integer getPrestige() {
        return prestige;
    }

    public void setPrestige(Integer prestige) {
        this.prestige = prestige;
    }

    public Date getGmtLastLogin() {
        return gmtLastLogin;
    }

    public void setGmtLastLogin(Date gmtLastLogin) {
        this.gmtLastLogin = gmtLastLogin;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

}
