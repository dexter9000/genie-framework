package com.genie.es.repository;

import com.genie.es.annotation.ESIndex;
import com.genie.es.exception.ElasticSearchException;
import com.genie.es.util.EntityUtil;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

/**
 * 简单ES处理类，支持单条数据的写入
 * @param <T>
 */
@Repository
public class SimpleElasticSearchRepository<T> implements ElasticSearchRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(SimpleElasticSearchRepository.class);

    private ElasticSearchOperations elasticSearchOperations;

    public SimpleElasticSearchRepository(ElasticSearchOperations elasticSearchOperations) {
        this.elasticSearchOperations = elasticSearchOperations;
    }

    @Override
    public void save(T entity) {
        log.debug("Save entity : {}", entity);
        String index = EntityUtil.getIndex(entity);

        elasticSearchOperations.create(index, EntityUtil.getType(entity),
                UUID.randomUUID().toString(), entity);
    }

    @Override
    public void save(Collection<T> entities) {
        log.debug("Save entitys, size : {}", entities.size());
        if(entities.isEmpty()){
            // 集合为空
            return;
        }

        T entity = entities.iterator().next();
        String index = EntityUtil.getIndex(entity);

        elasticSearchOperations.create(
                index,
                EntityUtil.getType(entity),
                entities);
    }

    public Page<T> findPage(QueryBuilder postFilter, String shardingId, Pageable pageable){
        return elasticSearchOperations.findPage(getIndex(shardingId), getType(), postFilter, pageable, EntityUtil.getGenericClass(this.getClass()));
    }


    private String getIndex(String shardingId) {
        Class<T> genericClass = EntityUtil.getGenericClass(this.getClass());
        ESIndex index = genericClass.getAnnotation(ESIndex.class);

        if (index == null) {
            throw new ElasticSearchException("Elastic Search Entity is null : " + genericClass.getTypeName());
        }
        return index.name();
    }

    private String getType() {
        return EntityUtil.getGenericClass(this.getClass()).getSimpleName();
    }

}
