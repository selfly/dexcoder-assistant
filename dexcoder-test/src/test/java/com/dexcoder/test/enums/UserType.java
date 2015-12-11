package com.dexcoder.test.enums;

import com.dexcoder.commons.enums.IEnum;

/**
 * Created by liyd on 2015-10-19.
 */
public enum UserType implements IEnum {

    ADMIN("1", "管理员"),

    VIP("2", "VIP"),

    MEMBER("3", "会员");

    UserType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
