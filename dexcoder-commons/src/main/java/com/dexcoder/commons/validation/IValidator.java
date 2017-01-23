package com.dexcoder.commons.validation;

/**
 * Created by liyd on 17/1/23.
 */
public interface IValidator {

    /***
     * 值校验
     *
     * @param obj
     * @return
     */
    boolean validate(Object obj);

    /**
     * 校验代码
     *
     * @return
     */
    String validateCode();

    /**
     * 校验失败错误信息
     *
     * @param validateValue
     * @param validateName
     * @return
     */
    String validateMsg(Object validateValue, String validateName);
}
