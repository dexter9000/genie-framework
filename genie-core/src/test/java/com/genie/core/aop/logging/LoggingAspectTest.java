package com.genie.core.aop.logging;

import com.genie.core.MicroServerApp;
import com.genie.core.properties.GenieProperties;
import com.genie.core.service.LogTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MicroServerApp.class},
        properties = "spring.profiles.active=dev"
)
@EnableConfigurationProperties({GenieProperties.class})
public class LoggingAspectTest {
    @Autowired
    private LogTestService service;

    @Test
    public void logAfterThrowing() {
        try {
            service.exceptionProcess();
            fail();
        } catch (Exception e) {
            //
        }
    }

    @Test
    public void logAround() {
        service.serviceProcess();
    }
}
