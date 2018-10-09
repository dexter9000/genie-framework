package com.genie.data.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CriteriaField {

    boolean enabled() default true;

    String filter() default "base";
}
