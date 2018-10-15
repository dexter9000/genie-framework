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

    /**
     * 索引名
     * @return 索引名
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 索引名
     * @return 索引名
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 分索引标识
     * @return 是否需要分索引
     */
    boolean shard() default false;

    /**
     * 分索引名模板
     * @return
     */
    String shardTemplate() default "{index}_{shardId}";
}
