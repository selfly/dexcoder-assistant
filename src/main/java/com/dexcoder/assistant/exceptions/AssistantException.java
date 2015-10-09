package com.dexcoder.assistant.exceptions;

/**
 * 自定义异常类
 *
 * Created by liyd on 6/27/14.
 */
public class AssistantException extends DexcoderException {

    /** serialVersionUID */
    private static final long serialVersionUID = -1921648378954132894L;

    /**
     * Constructor
     */
    public AssistantException() {
        super();
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
     * @param code the code
     * @param message the message
     */
    public AssistantException(String code, String message) {
        super(code, message);
    }
}
