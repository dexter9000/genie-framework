package com.genie.core.service;

import org.springframework.cache.annotation.Cacheable;

public class CacheService {

    @Cacheable
    public String getMember(String id){
        return "1";
    }
}
