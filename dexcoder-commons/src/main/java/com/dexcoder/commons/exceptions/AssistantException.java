package com.dexcoder.commons.exceptions;

import com.dexcoder.commons.enums.IEnum;

/**
 * 自定义异常类
 * <p/>
 * Created by liyd on 6/27/14.
 */
public class AssistantException extends DexcoderException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1921648378954132894L;

    public AssistantException(String message, Throwable e) {
        super(message, e);
        this.resultMsg = message;
    }

    /**
     * Instantiates a new AssistantException.
     *
     * @param e the e
     */
    public AssistantException(Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new AssistantException.
     *
     * @param iEnum
     */
    public AssistantException(IEnum iEnum) {
        super(iEnum);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public AssistantException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param code    the code
     * @param message the message
     */
    public AssistantException(String code, String message) {
        super(code, message);
    }
}
