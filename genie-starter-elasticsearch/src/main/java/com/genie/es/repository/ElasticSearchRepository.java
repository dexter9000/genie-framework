package com.genie.es.repository;

import java.util.Collection;

public interface ElasticSearchRepository<T> {

    /**
     * 保存单条数据
     * @param entity
     */
    void save(T entity);

    /**
     * 保存数据集合
     * @param entities
     */
    void save(Collection<T> entities);
}
