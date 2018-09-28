package com.genie.core.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 批量阻塞队列，对BlockingQueue做了封装
 * @param <T>
 */
public class BatchQueue<T> {
    private final Logger log = LoggerFactory.getLogger(BatchQueue.class);
    private static final long DEFAULT_FLUSH_TIME = 1000;
    private static final int DEFAULT_MAX_QUEUE_SIZE = 5000;
    private String key;
    private long flushTime;
    private BlockingQueue<T> queue;
    private long createTime;

    public BatchQueue(String key) {
        this(key, DEFAULT_FLUSH_TIME, DEFAULT_MAX_QUEUE_SIZE);
    }

    public BatchQueue(String key, long flushTime) {
        this(key, flushTime, DEFAULT_MAX_QUEUE_SIZE);
    }

    public BatchQueue(String key, long flushTime, int maxQueueSize) {
        this.key = key;
        this.flushTime = flushTime;
        this.createTime = System.currentTimeMillis();
        this.queue = new LinkedBlockingQueue<>(maxQueueSize);
    }

    /**
     * add new entity to queue
     * @param t
     * @return
     */
    public boolean add(T t) {
        try {
            queue.put(t);
            return true;
        } catch (InterruptedException e) {
            log.error("Put queue [" + key + "] error", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * pop entity from queue
     * @param batchSize
     * @return
     */
    public List<T> pop(int batchSize) {
        List<T> drained = new ArrayList<>();
        int num = queue.drainTo(drained, batchSize);
        log.info("pop num is {}", num);
        this.createTime = System.currentTimeMillis();
        return drained;
    }

    public int size() {
        return queue.size();
    }

    public String getKey() {
        return key;
    }

    public BlockingQueue<T> getQueue() {
        return queue;
    }

    /**
     * 根据超时时间判断队列是否需要刷新
     * @return
     */
    public boolean needFlush() {
        long liveTime = System.currentTimeMillis() - createTime;
        return liveTime > flushTime;
    }
}
