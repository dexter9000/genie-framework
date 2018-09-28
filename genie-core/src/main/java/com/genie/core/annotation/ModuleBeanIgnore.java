package com.genie.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 在module下不加载
 * TODO： 待实现
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ModuleBeanIgnore {
}
