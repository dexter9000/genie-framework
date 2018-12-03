package com.genie.core.batch;

import com.genie.core.exception.ShardingException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 批量队列集合，通过队列名称可以方便的切换处理的队列
 * @param <T>
 */
public class BatchQueueMap<T> {

    private static final int DEFAULT_FLUSH_TIME = 1000;

    private Map<String, BatchQueue> batchs = new ConcurrentHashMap<>();
    private int batchSize;
    private int flushTime;

    public BatchQueueMap(int batchSize) {
        this(batchSize, DEFAULT_FLUSH_TIME);
    }

    public BatchQueueMap(int batchSize, int flushTime) {
        this.batchSize = batchSize;
        this.flushTime = flushTime;
    }

    /**
     * add entity into queue map
     * @param key queue name
     * @param t entity
     * @return
     */
    public boolean add(String key, T t) {
        BatchQueue queue = this.batchs.get(key);
        if (queue == null) {
            queue = new BatchQueue(key, flushTime);
            this.batchs.put(key, queue);
        }
        return queue.add(t);
    }

    /**
     * @param key
     * @return
     * @throws ShardingException
     */
    public List<T> pop(String key) throws ShardingException {
        BatchQueue queue = this.batchs.get(key);
        if (queue == null) {
            throw new ShardingException("queue is null");
        }
        return queue.pop(this.batchSize);
    }

    /**
     * 刷新整个map，删除空队列
     */
    public void flush(){

        Iterator<Map.Entry<String, BatchQueue>> it = batchs.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, BatchQueue> item = it.next();
            if(item.getValue().size() <= 0){
                it.remove();
            }
        }
    }

    /**
     * 删除队列
     * @param key
     */
    public void remove(String key) {
        this.batchs.remove(key);
    }

    public BlockingQueue<T> getQueue(String key) {
        if (this.batchs.get(key) == null) {
            return null;
        }
        return this.batchs.get(key).getQueue();
    }


    public boolean isFullQueue(String indexName) {
        if (batchs.get(indexName).size() >= this.batchSize) {
            return true;
        }
        return false;
    }

    public boolean haveFullQueue() {
        Set<String> keyes = this.batchs.keySet();
        for (String key : keyes) {
            BatchQueue batchQueue = batchs.get(key);
            if (batchQueue != null && batchQueue.getQueue().size() >= this.batchSize) {
                return true;
            }
        }
        return false;
    }

    public BatchQueue getFullQueue() {
        Set<String> keyes = this.batchs.keySet();
        for (String key : keyes) {
            BatchQueue batchQueue = batchs.get(key);
            if (batchQueue != null && batchQueue.getQueue().size() >= this.batchSize) {
                return batchQueue;
            }
        }
        return null;
    }

    public BatchQueue nextQueue() {
        Set<String> keyes = this.batchs.keySet();
        for (String key : keyes) {
            BatchQueue batchQueue = batchs.get(key);
            if (batchQueue != null && !batchQueue.getQueue().isEmpty()) {
                return batchQueue;
            }
        }
        return null;
    }

    public BatchQueue nextFlushQueue() {
        Set<String> keyes = this.batchs.keySet();
        for (String key : keyes) {
            BatchQueue batchQueue = batchs.get(key);
            if (batchQueue != null && batchQueue.needFlush() && !batchQueue.getQueue().isEmpty()) {
                return batchQueue;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        Set<String> keyes = this.batchs.keySet();
        for (String key : keyes) {
            BatchQueue batchQueue = batchs.get(key);
            if (batchQueue != null && !batchQueue.getQueue().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
