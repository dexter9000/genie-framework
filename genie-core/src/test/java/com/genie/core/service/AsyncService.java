package com.genie.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    private final Logger log = LoggerFactory.getLogger(AsyncService.class);

    @Async("taskExecutor")
    public void doAsyncFunc1(int index) {
        log.info("Start async func 1 [{}]...", index);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Finish async func1 [{}]!", index);
    }

    @Async("taskExecutor2")
    public void doAsyncFunc2(int index) {
        log.info("Start async func 2 [{}]...", index);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Finish async func 2 [{}]!", index);
    }

}
