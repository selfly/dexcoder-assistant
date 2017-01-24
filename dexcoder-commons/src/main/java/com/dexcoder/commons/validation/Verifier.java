package com.dexcoder.commons.validation;

import com.dexcoder.commons.exceptions.DexcoderException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liyd on 17/1/23.
 */
public final class Verifier {

    /** 校验的元素信息 */
    private List<ValidatorElement> validatorElements;

    public Verifier() {
        validatorElements = new ArrayList<ValidatorElement>();
    }

    public static Verifier init() {
        return new Verifier();
    }

    public Verifier notNull(Object value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, new NotNullValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier notEmpty(Collection<?> value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, new NotEmptyValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier notBlank(String value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, new NotBlankValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier gtThan(int value, int expectVal, String name) {
        gtThan((long) value, (long) expectVal, name);
        return this;
    }

    public Verifier gtEq(int value, int expectVal, String name) {
        gtEq((long) value, (long) expectVal, name);
        return this;
    }

    public Verifier ltThan(int value, int expectVal, String name) {
        ltThan((long) value, (long) expectVal, name);
        return this;
    }

    public Verifier ltEq(int value, int expectVal, String name) {
        ltEq((long) value, (long) expectVal, name);
        return this;
    }

    public Verifier eq(int value, int expectVal, String name) {
        eq((long) value, (long) expectVal, name);
        return this;
    }

    public Verifier gtThan(long value, long expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new NumberValidator(NumberValidator.GT));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier gtEq(long value, long expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new NumberValidator(NumberValidator.GT_EQ));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier ltThan(long value, long expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new NumberValidator(NumberValidator.LT));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier ltEq(long value, long expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new NumberValidator(NumberValidator.LT_EQ));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier eq(long value, long expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new NumberValidator(NumberValidator.EQ));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier regex(String value, String regex, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new String[] { value, regex }, name,
            new RegexValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier eachNotNull(Collection<?> collection, String name) {
        ValidatorElement validatorElement = new ValidatorElement(collection, name,
            new CollectionEachNotNullValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier on(Object value, String name, Validator validator) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, validator);
        validatorElements.add(validatorElement);
        return this;
    }

    /**
     * 单独指定错误码
     * 
     * @param errorCode
     * @return
     */
    public Verifier errorCode(String errorCode) {
        getLastValidationElement().setErrorCode(errorCode);
        return this;
    }

    /**
     * 单独指定错误信息
     * 
     * @param errorMsg
     * @return
     */
    public Verifier errorMsg(String errorMsg) {
        getLastValidationElement().setErrorMsg(errorMsg);
        return this;
    }

    public void validate() {
        result(true);
    }

    public ValidationResult result() {
        return result(false);
    }

    private ValidationResult result(boolean invalidFast) {

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

    private ValidatorElement getLastValidationElement() {
        if (validatorElements.isEmpty()) {
            throw new DexcoderException("请先设置需要校验的元素");
        }
        return validatorElements.get(validatorElements.size() - 1);
    }

}
