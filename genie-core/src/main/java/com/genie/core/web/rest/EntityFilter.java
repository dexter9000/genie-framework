package com.genie.core.web.rest;

/**
 * 实体类型过滤器
 */
public interface EntityFilter {

    /**
     * 对实体类型进行统一加工
     * @param obj 原始值
     * @param <T> 实体类型
     * @return 加工后的结果
     */
    <T> T doEntityFilter(T obj);
}
