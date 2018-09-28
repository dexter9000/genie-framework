package com.genie.es.entity;


import com.genie.es.exception.ElasticSearchException;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class BatchQueueMap<T> {

    private Map<String, BatchQueue> batchs = new HashMap<>();
    private int batchSize;

    public BatchQueueMap(int batchSize) {
        this.batchSize = batchSize;
    }

    public boolean add(String key, T t) {
        BatchQueue queue = this.batchs.get(key);
        if (queue == null) {
            queue = new BatchQueue(key);
            this.batchs.put(key, queue);
        }
        return queue.add(t);
    }

    public List<T> pop(String key) {
        BatchQueue queue = this.batchs.get(key);
        if (queue == null) {
            throw new ElasticSearchException("queue is null");
        }
        return queue.pop(this.batchSize);
    }

    public void flush(){
        Iterator<Map.Entry<String, BatchQueue>> it = batchs.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, BatchQueue> item = it.next();
            if(item.getValue().size() <= 0){
                it.remove();
            }
        }
    }

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
