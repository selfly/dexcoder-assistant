package com.dexcoder.commons.mvc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.dexcoder.commons.enums.IEnum;
import com.dexcoder.commons.utils.EnumUtils;
import com.dexcoder.commons.utils.StrUtils;

/**
 * 枚举转换工厂
 * <p/>
 * User: liyd
 * Date: 14-1-20
 * Time: 下午9:11
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IEnumConverterFactory implements ConverterFactory<String, Enum> {

    /**
     * Get the converter to convert from S to target type T, where T is also an instance of R.
     *
     * @param targetType the target type to convert to
     * @return A converter from S to T
     */
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        if (IEnum.class.isAssignableFrom(targetType)) {
            return new IEnumConverter(targetType);
        }
        return null;
    }

    /**
     * 枚举转换内部类
     *
     * @param <T>
     */
    private class IEnumConverter<T extends Enum> implements Converter<String, T> {

        /**
         * 枚举类型
         */
        private final Class<T> enumType;

        public IEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        /**
         * 转换
         *
         * @param source
         * @return
         */
        public T convert(String source) {
            if (StrUtils.isBlank(source)) {
                return null;
            }
            return EnumUtils.getEnum(enumType, source);
        }
    }
}
