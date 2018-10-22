package com.genie.data.annotation;

import java.lang.annotation.*;

/**
 * 分片表标记
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingTable {

    String value() default "";
}
