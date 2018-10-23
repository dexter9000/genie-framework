package com.genie.modelgen.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to indicate that for this test it is ok to get compilation errors via {@link javax.tools.Diagnostic}.
 *
 * @author Hardy Ferentschik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface IgnoreCompilationErrors {
}