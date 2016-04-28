package com.dexcoder.commons.bean;

/**
 * Created by liyd on 16/4/27.
 */
public class LongIntegerConverter extends AbstractTypeConverter {

    public LongIntegerConverter(Class<?> sourceClass, Class<?> targetClass) {
        super(sourceClass, targetClass);
    }

    @Override
    public Object convert(Class<?> actualSourceClass, Class<?> actualTargetClass, Object value) {
        if (value == null) {
            return null;
        }

        if (actualTargetClass.equals(Long.class)) {
            return Long.valueOf((Integer) value);
        } else {
            return ((Long) value).intValue();
        }
    }
}
