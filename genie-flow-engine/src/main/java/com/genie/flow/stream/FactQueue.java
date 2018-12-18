package com.genie.flow.stream;

import com.genie.flow.domain.Fact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FactQueue {
    private static final Logger log = LoggerFactory.getLogger(FactQueue.class);

    private static Map<String, BlockingQueue> queueMap = new HashMap<>();
    private boolean isShutdown = false;

    public synchronized BlockingQueue<Fact> getQueue(String channel){
        BlockingQueue<Fact> queue = queueMap.get(channel);
        if(queue == null){
            queue = new LinkedBlockingQueue<>();
            queueMap.put(channel, queue);
        }
        return queue;
    }

    public void sendMessage(String channel, Fact fact){
        log.debug("sendMessage for channel: {}, [{}]", channel, fact);
        BlockingQueue<Fact> queue = getQueue(channel);
        try {
            queue.put(fact);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registry(String channel, FactQueueListen listen){
        log.info("registry listen for channel: {}", channel);
        BlockingQueue<Fact> queue = getQueue(channel);
        ListenThread thread = new ListenThread(queue, listen);

        thread.start();

    }

    public void shutdown() {
        log.info("ProcessQueueHandle shutdown...");
        isShutdown = true;
    }

    class ListenThread extends Thread {

        private BlockingQueue<Fact> queue;
        private FactQueueListen listen;

        public ListenThread(BlockingQueue<Fact> queue, FactQueueListen listen) {
            this.queue = queue;
            this.listen = listen;
        }

        @Override
        public void run() {

            while(true){
                try {
                    Fact fact = queue.take();
                    listen.receiveFact(fact);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isShutdown) {
                    break;
                }

            }
        }
    }
}
