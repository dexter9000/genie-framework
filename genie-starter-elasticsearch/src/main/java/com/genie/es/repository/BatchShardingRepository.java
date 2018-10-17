package com.genie.es.repository;

import com.genie.core.batch.BatchHandler;
import com.genie.es.config.ElasticSearchProperties;
import com.genie.es.util.EntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 批量分片数据处理类
 *
 * @param <T>
 */
@Repository
public class BatchShardingRepository<T> extends BatchHandler<T> {

    private static final Logger log = LoggerFactory.getLogger(BatchShardingRepository.class);

    private ElasticSearchOperations elasticSearchOperations;
    private AtomicLong start = new AtomicLong(System.currentTimeMillis());
    private AtomicLong emptyTime = new AtomicLong(System.currentTimeMillis()); // 队列闲置时间

    public BatchShardingRepository(ElasticSearchOperations elasticSearchOperations, ElasticSearchProperties elasticSearchProperties) {
        super(elasticSearchProperties.getBatchSize(),
            elasticSearchProperties.getFlushTime(),
            elasticSearchProperties.getThreadTimeout());

        this.elasticSearchOperations = elasticSearchOperations;
    }


    /**
     * 添加数据到队列
     *
     * @param entity
     * @return
     */
    public void save(T entity) {
        String index = EntityUtil.getShardingName(entity);
        insertBatch(index, entity);
    }

    @Override
    public void doProcess(String key, List<T> drained) {
        try {
            elasticSearchOperations.createNoBlock(key, EntityUtil.getType(drained.get(0)), drained);
        } catch (Exception e) {
            log.error("do drainToConsume error:", e);
        }
    }
}
