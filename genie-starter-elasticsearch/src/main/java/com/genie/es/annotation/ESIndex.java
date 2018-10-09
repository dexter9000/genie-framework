package com.genie.es.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 支持分索引的索引声明
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ESIndex {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean shard() default false;

    String shardTemplate() default "{index}_{shardId}";
}
