package com.genie.es.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BatchQueue<T> {

    private final Logger log = LoggerFactory.getLogger(BatchQueue.class);

    private String key;

    private BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    private long createTime;

    public BatchQueue(String key) {
        this.key = key;
        this.createTime = System.currentTimeMillis();
    }

    public boolean add(T t){
        return queue.add(t);
    }

    public List<T> pop(int batchSize){
        List<T> drained = new ArrayList<>();
        int num = queue.drainTo(drained, batchSize);
        log.info("return num :" + num);
        return drained;
    }

    public int size(){
        return queue.size();
    }

    public String getKey() {
        return key;
    }

    public BlockingQueue<T> getQueue() {
        return queue;
    }

    public long getCreateTime() {
        return createTime;
    }
}
