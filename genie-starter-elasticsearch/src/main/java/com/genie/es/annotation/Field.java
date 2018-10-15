package com.genie.es.annotation;

import com.fasterxml.jackson.annotation.JsonFilter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 字段声明，针对特殊需要转换字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonFilter("")
@Documented
public @interface Field {

    /**
     * json字段名
     * @return
     */
    @AliasFor(annotation = JsonFilter.class, attribute = "value")
    String value() default "";
}

