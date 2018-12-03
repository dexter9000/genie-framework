package com.genie.core.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 批量阻塞队列，对BlockingQueue做了封装
 * @param <T> 队列内容的类型
 */
public class BatchQueue<T> {
    private final Logger log = LoggerFactory.getLogger(BatchQueue.class);
    private static final long DEFAULT_FLUSH_TIME = 1000;    // 默认超时时间
    private static final int DEFAULT_MAX_QUEUE_SIZE = 5000; // 默认最大队列数量
    private String key;             // 队列名称，队列内容根据该名称控制
    private long flushTime;         // 刷新时间，非满队列处理的超时时间
    private BlockingQueue<T> queue; // 阻塞队列
    private long createTime;        // 队列创建时间

    /**
     * 构造函数
     * @param key 队列名称，队列内容根据该名称控制
     */
    public BatchQueue(String key) {
        this(key, DEFAULT_FLUSH_TIME, DEFAULT_MAX_QUEUE_SIZE);
    }

    /**
     * 构造函数
     * @param key 队列名称，队列内容根据该名称控制
     * @param flushTime 刷新时间，非满队列处理的超时时间
     */
    public BatchQueue(String key, long flushTime) {
        this(key, flushTime, DEFAULT_MAX_QUEUE_SIZE);
    }

    /**
     * 构造函数
     * @param key 队列名称，队列内容根据该名称控制
     * @param flushTime 刷新时间，非满队列处理的超时时间
     * @param maxQueueSize 最大队列数量
     */
    public BatchQueue(String key, long flushTime, int maxQueueSize) {
        this.key = key;
        this.flushTime = flushTime;
        this.createTime = System.currentTimeMillis();
        this.queue = new LinkedBlockingQueue<>(maxQueueSize);
    }

    /**
     * add new entity to queue
     * @param t 内容实体
     * @return 处理结果
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
     * @param batchSize 出队数量
     * @return 出队内容
     */
    public List<T> pop(int batchSize) {
        List<T> drained = new ArrayList<>();
        int num = queue.drainTo(drained, batchSize);
        log.info("pop num is {}", num);
        this.createTime = System.currentTimeMillis();
        return drained;
    }

    /**
     * 队列长度
     * @return
     */
    public int size() {
        return queue.size();
    }

    /**
     * 队列名称
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * 直接获取队列
     * @return
     */
    public BlockingQueue<T> getQueue() {
        return queue;
    }

    /**
     * 根据超时时间判断队列是否需要刷新
     * @return 是否需要刷新
     */
    public boolean needFlush() {
        long liveTime = System.currentTimeMillis() - createTime;
        return liveTime > flushTime;
    }
}
