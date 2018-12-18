package com.genie.schedule.utils;

import com.genie.schedule.service.dto.CronJobInfo;
import org.junit.Test;
import org.quartz.Trigger;

public class QuartZUtilTest {
    @Test
    public void testGetJobTrigger() {
        String executionTime = "0 0 9 27 7 ?";
        CronJobInfo cronJobInfo = new CronJobInfo();
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);

        Trigger trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        System.out.println(trigger);
    }
}
