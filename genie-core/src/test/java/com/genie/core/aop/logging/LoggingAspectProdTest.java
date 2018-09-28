package com.genie.core.aop.logging;

import com.genie.core.MicroServerApp;
import com.genie.core.properties.GenieProperties;
import com.genie.core.service.LogTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MicroServerApp.class},
        properties = {"spring.profiles.active=prod"})
public class LoggingAspectProdTest {

    @Autowired
    private LogTestService service;
    @Autowired
    private GenieProperties properties;

    @Test
    public void testProperties(){
        System.out.println(properties.getLoggingAspect().getPatterns());
    }

    @Test
    public void logAfterThrowing() {
        try {
            service.exceptionProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logAround() {
        service.serviceProcess();
    }
}
