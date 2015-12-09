package com.dexcoder.commons.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行消息返回结果
 * 
 * User: liyd
 * Date: 2/13/14
 * Time: 4:34 PM
 */
public class RunResult {

    /** is success */
    protected boolean      success = true;

    /** result code */
    protected String       code;

    /** error message */
    protected List<String> errors;

    /** result message */
    protected List<String> messages;

    /**
     * Constructor
     */
    public RunResult() {
    }

    /**
     * Constructor
     *
     * @param isSuccess the is success
     */
    public RunResult(boolean isSuccess) {
        this.success = isSuccess;
    }

    /**
     * Constructor
     *
     * @param isSuccess the is success
     * @param resultCode the result code
     */
    public RunResult(boolean isSuccess, String resultCode) {
        this.success = isSuccess;
        this.code = resultCode;
    }

    /**
     * 添加运行信息
     * 
     * @param message
     */
    public void addMessage(String message) {
        if (this.messages == null) {
            messages = new ArrayList<String>();
        }
        this.messages.add(message);
    }

    /**
     * 添加错误信息
     * 
     * @param error
     */
    public void addError(String error) {
        if (this.errors == null) {
            errors = new ArrayList<String>();
        }
        this.errors.add(error);
    }

    /**
     * 获取错误信息字符串，以;号分隔
     * 
     * @return
     */
    public String getStrErrors() {
        if (this.errors == null || this.errors.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String error : errors) {
            sb.append(error);
            sb.append(";");
        }
        return sb.toString();
    }

    /**
     * 获取结果信息字符串，以;号分隔
     * 
     * @return
     */
    public String getStrMessages() {

        if (this.messages == null || this.messages.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String resultMessage : messages) {
            sb.append(resultMessage);
            sb.append(";");
        }
        return sb.toString();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
