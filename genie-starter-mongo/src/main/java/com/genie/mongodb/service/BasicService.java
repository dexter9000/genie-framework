package com.genie.mongodb.service;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 数据基础功能模板
 * @param <ENTITY> 实体类型
 * @param <ID> ID类型
 * @param <R> Repository类型
 */
public abstract class BasicService<ENTITY, ID, R extends MongoRepository<ENTITY, ID>> {

    private final R repository;

    protected BasicService(R repository) {
        this.repository = repository;
    }

    public ENTITY save(ENTITY entity) {
        return repository.save(entity);
    }

    public void delete(ENTITY entity) {
        repository.delete(entity);
    }

    public void delById(ID id) {
        repository.deleteById(id);
    }

    public ENTITY findById(ID id) {
        return repository.findById(id).orElse(null);
    }
}
