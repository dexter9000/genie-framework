package com.genie.es.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingId {

    /**
     * 分配键顺序
     * @return
     */
    int order() default 0;


}
