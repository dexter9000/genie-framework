package com.genie.data.annotation;

import java.lang.annotation.*;

/**
 * 查询类型的实体标识，用于自动生成代码
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CriteriaEntity {

    String type();
}
