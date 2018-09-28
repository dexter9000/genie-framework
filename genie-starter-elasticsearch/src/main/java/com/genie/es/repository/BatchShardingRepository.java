package com.genie.es.repository;

import com.genie.es.config.ElasticSearchProperties;
import com.genie.es.entity.BatchQueue;
import com.genie.es.entity.BatchQueueMap;
import com.genie.es.util.EntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 批量分片数据处理类
 *
 * @param <T>
 */
@Repository
public class BatchShardingRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(BatchShardingRepository.class);

    private ElasticSearchOperations elasticSearchOperations;
    private int batchSize;
    private int flushTime;
    private int breakTime = 100;
    private int threadTimeout;
    private BatchQueueMap<T> queueMap;
    private AtomicBoolean isLooping = new AtomicBoolean(false);
    private AtomicLong start = new AtomicLong(System.currentTimeMillis());
    private AtomicLong emptyTime = new AtomicLong(System.currentTimeMillis()); // 队列闲置时间

    public BatchShardingRepository(ElasticSearchOperations elasticSearchOperations, ElasticSearchProperties elasticSearchProperties) {
        this.elasticSearchOperations = elasticSearchOperations;
        this.batchSize = elasticSearchProperties.getBatchSize();
        this.flushTime = elasticSearchProperties.getFlushTime();
        this.threadTimeout = elasticSearchProperties.getThreadTimeout();
        queueMap = new BatchQueueMap<>(this.batchSize);
    }


    /**
     * 添加数据到队列
     *
     * @param entity
     * @return
     */
    public void save(T entity) {
        String index = EntityUtil.getShardingName(entity);
        boolean result = queueMap.add(index, entity);
        if (!isLooping.get() && result) {
            isLooping.set(true);
            startLoop();
        }
    }


    /**
     * 启用线程根据条件是否到达处理数据
     */
    private synchronized void startLoop() {
        log.debug("do start Thread isLooping:{}", isLooping.get());
        Thread workThread = new Thread(() -> {
            start = new AtomicLong(System.currentTimeMillis());
            while (true) {
                long last = System.currentTimeMillis() - start.get();
                if (queueMap.haveFullQueue()) {
                    // 存在满队列
                    emptyTime = null;
                    drainToConsume(true);
                } else if ((!queueMap.isEmpty() && (last > flushTime))) {
                    // 触发刷新时间
                    emptyTime = null;
                    drainToConsume(false);
                } else if (queueMap.isEmpty()) {
                    if (emptyTime == null) {
                        // 首次监测到队列为空，记录队列闲置时间
                        emptyTime = new AtomicLong(System.currentTimeMillis());
                    } else if ((System.currentTimeMillis() - emptyTime.get()) > threadTimeout) {
                        // 对象闲置时间超时，回收线程
                        isLooping.set(false);
                        log.debug("do stop Thread isLooping:{}", isLooping.get());
                        break;
                    } else {
                        // 队列闲置间歇
                        try {
                            Thread.sleep(breakTime); //NOSONAR : 屏蔽本行代码的sonar检查
                        } catch (InterruptedException e) {
                            log.error("Interrupted!", e);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        });
        workThread.start();
    }

    /**
     * 处理队列中数据
     */
    private void drainToConsume(boolean fullQueue) {
        log.debug("do drainToConsume ...");
        while (true) {
            BatchQueue queue = null;
            if (fullQueue) {
                queue = queueMap.getFullQueue();
            } else {
                queue = queueMap.nextQueue();
            }
            if (queue == null) {
                break;
            }
            List<T> drained = queue.pop(batchSize);
            if (drained == null || drained.size() <= 0) {
                break;
            }
            log.debug("drained [{}] size is {}", queue.getKey(), drained.size());
            try {
                elasticSearchOperations.createNoBlock(queue.getKey(), EntityUtil.getType(drained.get(0)), drained);
            } catch (Exception e) {
                log.error("do drainToConsume error:", e);
            }
            queueMap.flush();
        }
        start.set(System.currentTimeMillis());
    }
}
