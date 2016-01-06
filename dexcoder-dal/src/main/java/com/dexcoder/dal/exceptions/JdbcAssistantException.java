package com.dexcoder.dal.exceptions;

import com.dexcoder.commons.exceptions.AssistantException;

/**
 * dal 异常
 * Created by liyd on 2015-12-4.
 */
public class JdbcAssistantException extends AssistantException {

    private static final long serialVersionUID = -2908094775089363098L;

    public JdbcAssistantException(String message, Throwable e) {
        super(message, e);
        this.resultMsg = message;
    }

    /**
     * Instantiates.
     *
     * @param e the e
     */
    public JdbcAssistantException(Throwable e) {
        super(e);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public JdbcAssistantException(String message) {
        super(message);
    }

}
