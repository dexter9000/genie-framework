package com.genie.core.annotation;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ZonedDateTime 格式转换，针对json对象中出现ZonedDateTime字段的格式转换声明
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
@JsonFormat
public @interface ZonedDateTimeFormat {

    /**
     * 转换字段类型
     * @return
     */
    @AliasFor(annotation = JsonFormat.class, attribute = "shape")
    JsonFormat.Shape shape() default STRING;

    @AliasFor(annotation = JsonFormat.class, attribute = "pattern")
    public String pattern() default "";

    @AliasFor(annotation = JsonFormat.class, attribute = "timezone")
    public String timezone() default "Z";

}
