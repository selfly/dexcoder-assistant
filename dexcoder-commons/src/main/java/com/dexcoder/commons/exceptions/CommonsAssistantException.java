package com.dexcoder.commons.exceptions;

import com.dexcoder.commons.enums.IEnum;

/**
 * Created by liyd on 2015-12-9.
 */
public class CommonsAssistantException extends AssistantException {

    private static final long serialVersionUID = 7607256205561930553L;

    public CommonsAssistantException(String message, Throwable e) {
        super(message, e);
        this.resultMsg = message;
    }

    /**
     * Instantiates a new CommonsAssistantException.
     *
     * @param e the e
     */
    public CommonsAssistantException(Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new CommonsAssistantException.
     *
     * @param iEnum
     */
    public CommonsAssistantException(IEnum iEnum) {
        super(iEnum);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public CommonsAssistantException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param code    the code
     * @param message the message
     */
    public CommonsAssistantException(String code, String message) {
        super(code, message);
    }
}
