package com.dexcoder.dal.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by liyd on 2016-1-4.
 */
@Target({ TYPE })
@Retention(RUNTIME)
public @interface Table {

    String name();

    String alias() default "";

    String pkColumn() default "";

    String pkField() default "";

    Class<?> mappingHandler() default Object.class;
}
