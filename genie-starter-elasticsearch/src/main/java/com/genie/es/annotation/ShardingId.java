package com.genie.es.annotation;

import java.lang.annotation.*;

/**
 * 分索引标记字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingId {

    /**
     * 分配键顺序
     * @return
     */
    int order() default 0;

    /**
     * 是否需要保存字段
     * @return
     */
    boolean storage() default true;
}
