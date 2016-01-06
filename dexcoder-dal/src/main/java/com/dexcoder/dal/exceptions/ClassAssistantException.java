package com.dexcoder.dal.exceptions;

import com.dexcoder.commons.exceptions.AssistantException;

/**
 * Created by liyd on 2015-12-4.
 */
public class ClassAssistantException extends AssistantException {

    private static final long serialVersionUID = 776718620724899656L;

    public ClassAssistantException(String message, Throwable e) {
        super(message, e);
        this.resultMsg = message;
    }

    /**
     * Instantiates.
     *
     * @param e the e
     */
    public ClassAssistantException(Throwable e) {
        super(e);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public ClassAssistantException(String message) {
        super(message);
    }
}
