package com.dexcoder.assistant.exceptions;

import com.dexcoder.assistant.enums.IEnum;

/**
 * 自定义异常类
 *
 * Created by liyd on 6/27/14.
 */
public class DexcoderException extends RuntimeException {

    /** Exception code */
    protected String resultCode = "UN_KNOWN_EXCEPTION";

    /** Exception message */
    protected String resultMsg  = "未知异常";

    /**
     * Constructor
     */
    public DexcoderException() {
        super();
    }

    /**
     * Instantiates a new DexcoderException.
     *
     * @param e the e
     */
    public DexcoderException(IEnum e) {
        super(e.getDesc());
        this.resultCode = e.getCode();
        this.resultMsg = e.getDesc();
    }

    /**
     * Instantiates a new DexcoderException.
     *
     * @param e the e
     */
    public DexcoderException(Throwable e) {
        super(e);
        this.resultMsg = e.getMessage();
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public DexcoderException(String message) {
        super(message);
        this.resultMsg = message;
    }

    /**
     * Constructor
     *
     * @param code the code
     * @param message the message
     */
    public DexcoderException(String code, String message) {
        super(message);
        this.resultCode = code;
        this.resultMsg = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
