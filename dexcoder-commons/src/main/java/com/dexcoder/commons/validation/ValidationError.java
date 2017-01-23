package com.dexcoder.commons.validation;

/**
 * 内部使用的验证结果包含的错误
 *
 * @author zhangxu
 */
public class ValidationError {

    /**
     * 错误字段名字
     */
    private String name;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 错误值
     */
    private Object invalidValue;

    @Override
    public String toString() {
        return "ValidationError{" + "name=" + name + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg
               + ", invalidValue=" + invalidValue + "}";
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ValidationError setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ValidationError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public String getName() {
        return name;
    }

    public ValidationError setName(String name) {
        this.name = name;
        return this;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public ValidationError setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
        return this;
    }

}
