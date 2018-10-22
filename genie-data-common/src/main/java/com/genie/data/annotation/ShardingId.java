package com.genie.data.annotation;

import java.lang.annotation.*;

/**
 * 分片键标记
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingId {

}
