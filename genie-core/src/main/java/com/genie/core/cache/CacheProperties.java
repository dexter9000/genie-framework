package com.genie.core.cache;

public class CacheProperties {

    private boolean enabled;

    private String cacheName;

    /**
     * 缓存范围，有三个值
     * SYSTEM: 整个系统
     * SERVICE: 相同服务
     * INSTANCE: 当前实例
     */
    private String cacheScope;


}
