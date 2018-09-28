package com.genie.core.service;

import com.genie.core.MicroServerApp;
import com.genie.core.properties.GenieProperties;
import com.genie.core.utils.ThreadUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MicroServerApp.class},
    properties = "spring.profiles.active=dev"
)
@EnableConfigurationProperties({GenieProperties.class})
public class AsyncServiceTest {

    private final Logger log = LoggerFactory.getLogger(AsyncServiceTest.class);

    @Autowired
    private AsyncService asyncService;

    @Test
    public void doAsyncFunc() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            asyncService.doAsyncFunc1(i);
            asyncService.doAsyncFunc2(i);
        }
        System.out.println("call func finish!");

        Thread.sleep(10000);
    }

    @Test
    public void testExecutorService() throws InterruptedException {
        ExecutorService executorService = ThreadUtil.newFixedThreadPool(2, 2);

        for(int i=0;i<10;i++){
            executorService.submit(new TestRunner(i));
        }

        System.out.println("===Finish!");
        Thread.sleep(10000);
    }

    @Test
    public void testThreadPoolTaskExecutor() throws InterruptedException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("server-Executor-");
        executor.initialize();
        MDC.put("THREAD_ID", String.valueOf(Thread.currentThread().getId()));

        log.info(MDC.get("THREAD_ID"));
        for(int i=0;i<10;i++){
            executor.submit(new TestRunner(i));
        }
        MDC.clear();

        System.out.println("===Finish!");
        Thread.sleep(10000);
    }

    class TestRunner implements Runnable {
        private final Logger log = LoggerFactory.getLogger(TestRunner.class);

        private int index;

        public TestRunner(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            log.info("Start TestRunner [{}]...", index);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("End TestRunner [{}]...", index);
        }
    }

}
