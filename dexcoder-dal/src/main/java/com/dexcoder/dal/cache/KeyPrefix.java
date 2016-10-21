package com.dexcoder.dal.cache;

import com.dexcoder.commons.enums.IEnum;

/**
 * Created by liyd on 16/10/9.
 */
public enum KeyPrefix implements IEnum {

    LIST("L", "列表"),

    GET("G", "get"),

    SINGLE("S", "single");

    KeyPrefix(String code, String desc) {
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
