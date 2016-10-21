package com.dexcoder.dal.build;

import org.omg.CORBA.TRANSIENT;

/**
 * Created by liyd on 2015-12-4.
 */
public enum AutoFieldType {

    NORMAL,

    INSERT,

    UPDATE,

    WHERE,

    INCLUDE,

    EXCLUDE,

    ORDER_BY_ASC,

    ORDER_BY_DESC,

    BRACKET_BEGIN,

    BRACKET_END,

    /** 函数 */
    FUNC,

    /** 拼装sql时忽略 */
    TRANSIENT
}
