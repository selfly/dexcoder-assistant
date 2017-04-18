package com.dexcoder.commons.validation;

import com.dexcoder.commons.enums.IEnum;
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

    public Verifier notEmpty(Object value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name, new NotEmptyValidator());
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier notBlank(String value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name,
            new StringValidator(StringValidator.NOT_BLANK));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier minLength(String value, int minLength, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, minLength }, name,
            new StringValidator(StringValidator.MIN_LENGTH));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier maxLength(String value, int maxLength, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, maxLength }, name,
            new StringValidator(StringValidator.MAX_LENGTH));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier eqLength(String value, int maxLength, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, maxLength }, name,
            new StringValidator(StringValidator.EQ_LENGTH));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier mustEmpty(Collection<?> value, String name) {
        ValidatorElement validatorElement = new ValidatorElement(value, name,
            new CollectionValidator(CollectionValidator.MUST_EMPTY));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier mustFalse(Boolean bool, String name) {
        ValidatorElement validatorElement = new ValidatorElement(bool, name,
            new BooleanValidator(BooleanValidator.FALSE));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier mustTrue(Boolean bool, String name) {
        ValidatorElement validatorElement = new ValidatorElement(bool, name,
            new BooleanValidator(BooleanValidator.TRUE));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier minSize(Collection<?> value, int minSize, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, minSize }, name,
            new CollectionValidator(CollectionValidator.MIN_SIZE));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier maxSize(Collection<?> value, int maxSize, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, maxSize }, name,
            new CollectionValidator(CollectionValidator.MAX_SIZE));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier eqSize(Collection<?> value, int eqSize, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, eqSize }, name,
            new CollectionValidator(CollectionValidator.EQ_SIZE));
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

    public Verifier mustEq(int value, int expectVal, String name) {
        mustEq((long) value, (long) expectVal, name);
        return this;
    }

    public Verifier mustEq(String value, String expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new StringValidator(StringValidator.EQ));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier notEq(String value, String expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new StringValidator(StringValidator.NOT_EQ));
        validatorElements.add(validatorElement);
        return this;
    }

    public Verifier mustEqIgnoreCase(String value, String expectVal, String name) {
        ValidatorElement validatorElement = new ValidatorElement(new Object[] { value, expectVal }, name,
            new StringValidator(StringValidator.EQ_IGNORE_CASE));
        validatorElements.add(validatorElement);
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

    public Verifier mustEq(long value, long expectVal, String name) {
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

    public Verifier with(boolean b) {
        if (!b) {
            removeLastValidationElement();
        }
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

    /**
     * 单独指定错误信息
     *
     * @param iEnum
     * @return
     */
    public Verifier error(IEnum iEnum) {
        ValidatorElement lastValidationElement = getLastValidationElement();
        lastValidationElement.setErrorCode(iEnum.getCode());
        lastValidationElement.setErrorMsg(iEnum.getDesc());
        return this;
    }

    public Verifier validate() {
        result(true);
        return this;
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
        validatorElements.clear();
        return result;
    }

    private ValidatorElement getLastValidationElement() {
        if (validatorElements.isEmpty()) {
            throw new DexcoderException("请先设置需要校验的元素");
        }
        return validatorElements.get(validatorElements.size() - 1);
    }

    private ValidatorElement removeLastValidationElement() {
        if (validatorElements.isEmpty()) {
            throw new DexcoderException("请先设置需要校验的元素");
        }
        return validatorElements.remove(validatorElements.size() - 1);
    }

}
