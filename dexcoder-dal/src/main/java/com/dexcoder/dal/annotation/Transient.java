package com.dexcoder.dal.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by liyd on 2015-12-31.
 */
@Target({ METHOD })
@Retention(RUNTIME)
public @interface Transient {
}
