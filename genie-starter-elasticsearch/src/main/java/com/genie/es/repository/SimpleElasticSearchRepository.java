package com.genie.es.repository;

import com.genie.es.util.EntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
