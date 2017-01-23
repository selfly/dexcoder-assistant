package com.dexcoder.commons.validation;

import com.dexcoder.commons.exceptions.DexcoderException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liyd on 17/1/23.
 */
public final class Validator {

    /** 校验的元素信息 */
    private List<ValidatorElement> validatorElements;

    public Validator() {
        validatorElements = new ArrayList<ValidatorElement>();
    }

    public static void main(String[] args) {

        String email = null;

        String email2 = " ";

        List<String> list = null;

        ValidationResult result = Validator.init().notNull(email, "邮箱").notBlank(email2, "邮箱2").notEmpty(list, "列表")
            .doValidate(false);

        System.out.println(result.isSuccess());
        for (ValidationError validationError : result.getErrors()) {
            System.out.println(validationError.getErrorCode());
            System.out.println(validationError.getErrorMsg());
        }

    }

    public static Validator init() {
        return new Validator();
    }

    public void doValidate() {
        doValidate(true);
    }

    public ValidationResult doValidate(boolean invalidFast) {

        ValidationResult result = new ValidationResult(true);

        for (ValidatorElement validatorElement : validatorElements) {

            boolean validate = validatorElement.getValidator().validate(validatorElement.getValidateValue());

            if (!validate) {
                String errorCode = validatorElement.getErrorCode();
                String errorMsg = validatorElement.getErrorMsg();
                if (invalidFast) {
                    throw new DexcoderException(errorCode, errorMsg);
                } else {

                    ValidationError validationError = new ValidationError();
                    validationError.setErrorCode(errorCode).setErrorMsg(errorMsg)
                        .setName(validatorElement.getValidateName())
                        .setInvalidValue(validatorElement.getValidateValue());

                    result.setIsSuccess(false);
                    result.addError(validationError);
                }
            }
        }
        return result;
    }

    public Validator notNull(Object value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, new NotNullValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Validator notEmpty(Collection<?> value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, new NotEmptyValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Validator notBlank(String value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, new NotBlankValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    /**
     * 单独指定错误码
     * 
     * @param errorCode
     * @return
     */
    public Validator errorCode(String errorCode) {
        getLastValidationElement().setErrorCode(errorCode);
        return this;
    }

    /**
     * 单独指定错误信息
     * 
     * @param errorMsg
     * @return
     */
    public Validator errorMsg(String errorMsg) {
        getLastValidationElement().setErrorMsg(errorMsg);
        return this;
    }

    private ValidatorElement getLastValidationElement() {
        if (validatorElements.isEmpty()) {
            throw new DexcoderException("请先设置需要校验的元素");
        }
        return validatorElements.get(validatorElements.size() - 1);
    }

}
