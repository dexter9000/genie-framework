package com.genie.es.repository;

import java.util.Collection;

/**
 * ElasticSearch持久化接口，由于ElasticSearch是异步处理模式，为了提高性能，不支持持久化后返回结果
 * @param <T>
 */
public interface ElasticSearchRepository<T> {

    /**
     * 保存单条数据
     * @param entity 实体对象
     */
    void save(T entity);

    /**
     * 保存数据集合
     * @param entities 实体对象集合
     */
    void save(Collection<T> entities);
}
