package com.genie.core.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 批量处理器
 * 当有数据存入处理器时，会启动消费线程，在指定的消费策略触发时消费数据。
 * 当所有数据消费完成后一段时间内没有新的数据，则会结束消费线程，待新的数据存入后再创建消费线程。
 *
 * @param <T>
 */
public abstract class BatchHandler<T> {

    private static final Logger log = LoggerFactory.getLogger(BatchHandler.class);

    protected int batchSize;        // 批量大小
    protected int flushTime;        // 队列超时时间
    protected int breakTime = 100;  // 延时时间，防止
    protected int threadTimeout;    // 线程超时时间，当到达该时间切队列为空，则删除线程
    protected AtomicBoolean isLooping = new AtomicBoolean(false);
    protected BatchQueueMap<T> queueMap;

    public BatchHandler(int batchSize, int flushTime, int threadTimeout) {
        this.batchSize = batchSize;
        this.flushTime = flushTime;
        this.threadTimeout = threadTimeout;
        this.queueMap = new BatchQueueMap<>(this.batchSize, flushTime);
    }

    /**
     * 将处理对象插入批量队列
     *
     * @param key   分片键
     * @param value 处理对象
     * @return 插入结果
     */
    public boolean insertBatch(String key, T value) {
        boolean result = queueMap.add(key, value);

        // 判断线程是否开启
        if (!isLooping.get() && result) {
            isLooping.set(true);
            startBatchConsumeThread();
        }
        return result;
    }

    /**
     * 启动线程
     */
    public synchronized void startBatchConsumeThread() {
        log.debug("start BatchConsumeThread...");
        Thread wordThread = new BatchConsumeThread();
        wordThread.setName("BatchConsumeThread");
        wordThread.start();
    }

    /**
     * @param queue
     */
    public void drainToConsume(BatchQueue<T> queue) {
        log.debug("drainToConsume start...");
        if (queue == null || queue.size() <= 0) {
            log.debug("drainToConsume queue is empty");
            return;
        }
        List<T> drained = queue.pop(batchSize);
        if (drained == null || drained.isEmpty()) {
            log.debug("drainToConsume queue data is empty");
            return;
        }
        log.debug("do drain size : {}", drained.size());
        doProcess(drained);
    }

    /**
     * 数据处理方法
     *
     * @param drained
     */
    public abstract void doProcess(List<T> drained);

    /**
     * 刷新线程，用于在满足条件时处理队列数据
     */
    class BatchConsumeThread extends Thread {
        private long emptyTime = 0;

        @Override
        public void run() {
            log.debug("Start BatchHandler Thread...");
            while (true) {
                boolean fullQueue = queueMap.haveFullQueue();
                boolean emptyQueue = queueMap.isEmpty();
                if (fullQueue) {    // 队列满
                    emptyTime = 0;
                    BatchQueue queue = queueMap.getFullQueue();
                    drainToConsume(queue);
                } else if (!emptyQueue) {   // 队列不满，但是达到超时时间
                    emptyTime = 0;
                    BatchQueue queue = queueMap.nextFlushQueue();
                    if (queue != null) {
                        drainToConsume(queue);
                    } else {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            log.error("sleep error", e);
                            Thread.currentThread().interrupt();
                        }
                    }
                } else if (emptyQueue) {
                    if (emptyTime == 0) {
                        emptyTime = System.currentTimeMillis();
                    } else if ((System.currentTimeMillis() - emptyTime) > threadTimeout) {
                        isLooping.set(false);
                        log.debug("do stop Thread isLooping:{}", isLooping.get());
                        break;
                    } else {
                        try {
                            Thread.sleep(breakTime);    // NOSONAR : 屏蔽Sonar检查
                        } catch (InterruptedException e) {
                            log.error("sleep error", e);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                queueMap.flush();
            }
        }
    }
}
