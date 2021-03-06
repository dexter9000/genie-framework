package com.genie.es.repository;

import com.genie.es.annotation.ShardingId;
import com.alibaba.fastjson.JSONObject;
import com.genie.es.util.EntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 简单分表ES处理类，支持单条数据自动根据{@link ShardingId}注解写入
 *
 * @param <T> 泛型
 */
@Repository
public class ShardingElasticSearchRepository<T> implements ElasticSearchRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(ShardingElasticSearchRepository.class);

    private ElasticSearchOperations elasticSearchOperations;

    public ShardingElasticSearchRepository(ElasticSearchOperations elasticSearchOperations) {
        this.elasticSearchOperations = elasticSearchOperations;
    }

    public T get(String index, String id){
        log.debug("Query by id : {}", id);
        String result = elasticSearchOperations.get(index, getType(), id);
        return JSONObject.parseObject(result, (Class<T>) EntityUtil.getGenericClass(this.getClass()));
    }

    private String getType() {
        return EntityUtil.getGenericClass(this.getClass()).getSimpleName();
    }

    @Override
    public void save(T entity) {
        String index =  EntityUtil.getShardingName(entity);
        String id =  UUID.randomUUID().toString();
        log.debug("Save entity with index : {}, id : {}", index, id);
        elasticSearchOperations.create(index, EntityUtil.getType(entity), id, entity);
    }

    public String saveObject(T entity) {
        String index = EntityUtil.getShardingName(entity);
        String id =  UUID.randomUUID().toString();
        elasticSearchOperations.create(index, EntityUtil.getType(entity), id, entity);
        return  id;
    }

    @Override
    public void save(Collection<T> entities) {
        if (entities.isEmpty()) {
            // 集合为空
            return;
        }

        Map<String, List<T>> entityMap = new HashMap<>();

        for (T entity : entities) {
            String index = EntityUtil.getShardingName(entity);

            List<T> shardingEntities = entityMap.get(index);

            if (shardingEntities == null) {
                shardingEntities = new ArrayList<>();
                entityMap.put(index, shardingEntities);
            }
            shardingEntities.add(entity);
        }
        entityMap.entrySet().stream().forEach(entry -> {
            entry.getKey();
            entry.getValue();
            elasticSearchOperations.create(
                    entry.getKey(),
                    EntityUtil.getType(entry.getValue().get(0)),
                    entry.getValue());
        });
    }
}
