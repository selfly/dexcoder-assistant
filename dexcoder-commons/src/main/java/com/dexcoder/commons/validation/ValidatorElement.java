package com.dexcoder.commons.validation;

import com.dexcoder.commons.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 17/1/23.
 */
public class ValidatorElement {

    /**
     * 验证器
     */
    private IValidator validator;

    /**
     * 待验证对象
     */
    private Object     validateValue;

    /** 校验对象的名称 一般中文备注名 非属性名 */
    private String     validateName;

    /** 指定的错误码 */
    private String     errorCode;

    /** 指定的错误信息 */
    private String     errorMsg;

    /**
     * create
     *
     * @param validateValue    待验证对象
     * @param validator 验证器
     * @param validateName 
     */
    public ValidatorElement(Object validateValue, String validateName, IValidator validator) {
        this.validateValue = validateValue;
        this.validateName = validateName;
        this.validator = validator;
    }

    public String getErrorCode() {
        //为空，自动生成一个唯一code
        if (StringUtils.isBlank(errorCode)) {
            this.errorCode = new StringBuilder(UUIDUtils.getUUID16(validateName.getBytes())).append(".")
                .append(validator.validateCode()).toString();
        }
        return errorCode;
    }

    public String getErrorMsg() {
        if (StringUtils.isBlank(errorMsg)) {
            this.errorMsg = validator.validateMsg(validateValue, validateName);
        }
        return errorMsg;
    }

    public IValidator getValidator() {
        return validator;
    }

    public void setValidator(IValidator validator) {
        this.validator = validator;
    }

    public Object getValidateValue() {
        return validateValue;
    }

    public void setValidateValue(Object validateValue) {
        this.validateValue = validateValue;
    }

    public String getValidateName() {
        return validateName;
    }

    public void setValidateName(String validateName) {
        this.validateName = validateName;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
