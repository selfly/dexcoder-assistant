package com.dexcoder.commons.validation;

import java.math.BigDecimal;

/**
 * 最小值验证
 * 
 * Created by liyd on 17/1/24.
 */
public class NumberValidator implements Validator {

    /** 表示大于 */
    public static final int GT    = 0;

    /** 表示小于 */
    public static final int LT    = 1;

    /** 表示大于等于 */
    public static final int GT_EQ = 2;

    /** 表示小于等于 */
    public static final int LT_EQ = 3;

    /** 表示等于 */
    public static final int EQ    = 4;

    private int             type;

    public NumberValidator(int type) {
        this.type = type;
    }

    public boolean validate(Object value) {
        Object[] values = (Object[]) value;
        BigDecimal val = new BigDecimal(String.valueOf(values[0]));
        BigDecimal expectVal = new BigDecimal(String.valueOf(values[1]));
        int i = val.compareTo(expectVal);
        switch (type) {
            case GT:
                return i > 0;
            case LT:
                return i < 0;
            case GT_EQ:
                return i >= 0;
            case LT_EQ:
                return i <= 0;
            case EQ:
                return i == 0;
            default:
                throw new UnsupportedOperationException("不支持的数字对比操作");
        }
    }

    public String validateCode() {
        switch (type) {
            case GT:
                return "must.gt";
            case LT:
                return "must.lt";
            case GT_EQ:
                return "must.gt.eq";
            case LT_EQ:
                return "must.lt.eq";
            case EQ:
                return "must.eq";
            default:
                return "";
        }
    }

    public String validateMsg(Object value, String validateName) {
        Object[] values = (Object[]) value;
        switch (type) {
            case GT:
                return validateName + "必须大于" + values[1];
            case LT:
                return validateName + "必须小于" + values[1];
            case GT_EQ:
                return validateName + "必须大于等于" + values[1];
            case LT_EQ:
                return validateName + "必须小于等于" + values[1];
            case EQ:
                return validateName + "必须等于" + values[1];
            default:
                return "";
        }
    }
}
