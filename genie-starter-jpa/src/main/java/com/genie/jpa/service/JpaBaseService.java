package com.genie.jpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 基础Service抽象类
 * 提供基本的CRUD功能
 * @param <T> 元数据类型
 * @param <ID> 主键类型
 */
public abstract class JpaBaseService<T, ID extends Serializable> {

    private final Logger log = LoggerFactory.getLogger(JpaBaseService.class);

    private final JpaRepository<T, ID> repository;

    private String entityName;

    protected JpaBaseService(JpaRepository repository) {
        this.repository = repository;
    }

    protected String getEntityName() {
        if (entityName == null) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            entityName = params[0].getTypeName();
        }
        return entityName;
    }

    /**
     * Save a entity.
     *
     * @param entity the entity to save
     * @return the persisted entity
     */
    @Transactional
    public T save(T entity) {
        log.debug("Request to save {} : {}", getEntityName(), entity);
        entity = repository.save(entity);
        return entity;
    }

    /**
     * Get all the entities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        log.debug("Request to get all {}", getEntityName());
        return repository.findAll(pageable);
    }

    /**
     * Get one entity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        log.debug("Request to get {} : {}", getEntityName(), id);
        return repository.getOne(id);
    }

    /**
     * Delete the  entity by id.
     *
     * @param id the id of the entity
     */
    public void delete(ID id) {
        log.debug("Request to delete {} : {}", getEntityName(), id);
        repository.deleteById(id);
    }

}
