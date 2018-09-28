package com.genie.es.annotation;

import com.fasterxml.jackson.annotation.JsonFilter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonFilter("")
@Documented
public @interface Field {

    @AliasFor(annotation = JsonFilter.class, attribute = "value")
    String value() default "";
}

