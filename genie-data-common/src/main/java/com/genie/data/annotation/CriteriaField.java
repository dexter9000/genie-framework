package com.genie.data.annotation;

import java.lang.annotation.*;

/**
 * 查询字段表示，用于自动生成代码
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CriteriaField {

    boolean enabled() default true;

    String filter() default "base";
}
