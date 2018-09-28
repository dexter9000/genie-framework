package com.genie.core.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 日志打印切面，对方法的入口和出口打印日志
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface LoggingPointcut {
}
