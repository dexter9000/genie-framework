package com.genie.schedule.service;

import com.genie.schedule.MicroServerApp;
import com.genie.schedule.service.dto.CronJobInfo;
import com.genie.schedule.utils.TaskHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MicroServerApp.class})
public class TriggerTest {

    @Autowired
    private QuartzTaskService service;

    @Test
    public void test() throws SchedulerException, InterruptedException {
        CronJobInfo cronJobInfo = new CronJobInfo();

        cronJobInfo.setCronExpression("");

        service.scheduleCronJobs(cronJobInfo, TaskHistory.class);

        Thread.sleep(5000);
    }
}
