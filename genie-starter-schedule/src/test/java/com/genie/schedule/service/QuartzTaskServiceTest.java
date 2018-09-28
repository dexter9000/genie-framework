package com.genie.schedule.service;

import com.genie.schedule.MicroServerApp;
import com.genie.schedule.service.dto.CronJobInfo;
import com.genie.schedule.service.dto.JobDetailDTO;
import com.genie.schedule.service.dto.SimpleJobInfo;
import com.genie.schedule.utils.QuartzUtil;
import com.genie.schedule.utils.TaskHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.quartz.TriggerBuilder.newTrigger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MicroServerApp.class}
//        ,properties = {"spring.profiles.active=prod",
//                "quartz.enabled=true",
//                "quartz.properties.org.quartz.scheduler.instanceName=QuartzScheduler",
//                "quartz.properties.org.quartz.scheduler.instanceId=AUTO",
//                "quartz.properties.org.quartz.scheduler.rmi.proxy=false",
//                "quartz.properties.org.quartz.scheduler.skipUpdateCheck=true",
//                "quartz.properties.org.quartz.scheduler.jmx.export=true",
//                "quartz.properties.org.quartz.scheduler.wrapJobExecutionInUserTransaction=false",
//                "quartz.properties.org.quartz.scheduler.dbFailureRetryInterval=1500",
//                "quartz.properties.org.quartz.threadPool.class=org.quartz.simpl.SimpleThreadPool",
//                "quartz.properties.org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread=true",
//                "quartz.properties.org.quartz.threadPool.threadCount=5",
//                "quartz.properties.org.quartz.threadPool.threadPriority=5",
//                "quartz.properties.org.quartz.jobStore.class=com.novemberain.quartz.mongodb.MongoDBJobStore",
//                "quartz.properties.org.quartz.jobStore.mongoUri=${spring.data.mongodb.uri}",
//                "quartz.properties.org.quartz.jobStore.dbName=${spring.data.mongodb.database}",
//                "quartz.properties.org.quartz.jobStore.collectionPrefix=QRTZ_"}
)
public class QuartzTaskServiceTest {
    @Autowired
    private QuartzTaskService quartzTaskService;

    private Trigger trigger;
    private CronJobInfo cronJobInfo = new CronJobInfo();
    @Test
    public void scheduleJobs() throws SchedulerException, InterruptedException {
        String executionTime = "2 * * * * ?";
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);

        trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        quartzTaskService.scheduleJobs(trigger, cronJobInfo, TaskHistory.class);
        SimpleJobInfo simpleJobInfo = new SimpleJobInfo();
        Date startTime = new Date(0);
        Date endTime = new Date(1000);
        simpleJobInfo.setStartTime(startTime);
        simpleJobInfo.setEndTime(endTime);
        simpleJobInfo.setRate("1");
        simpleJobInfo.setTimes("1");
        simpleJobInfo.setJobName("id7");
        simpleJobInfo.setJobGroup("Test");
        simpleJobInfo.setJobType("TaskHistory");
        String rate = simpleJobInfo.getRate();
        String times = simpleJobInfo.getTimes();
        String type = simpleJobInfo.getJobType();
        String[] argsNane = simpleJobInfo.getArgsNames();
        String[] argsValues = simpleJobInfo.getArgsValues();
        String jobType = simpleJobInfo.getJobType();
        endTime = simpleJobInfo.getEndTime();
        startTime = simpleJobInfo.getStartTime();
        Date date = new Date();
        SimpleTrigger simpleTrigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger1", "Test")
                .startAt(date)
                .forJob("id7", "Test")
                .build();
        Long count = simpleTrigger.getRepeatInterval();
        quartzTaskService.scheduleJobs(simpleTrigger, simpleJobInfo, TaskHistory.class);
    }

    @Test
    public void rescheduleJob() throws SchedulerException, InterruptedException {
        String executionTime = "2 10 10 10 OCT ?";
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id1");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);

        trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        quartzTaskService.scheduleJobs(trigger, cronJobInfo, TaskHistory.class);
        quartzTaskService.rescheduleJob(trigger, cronJobInfo, TaskHistory.class);
    }

    @Test
    public void queryJob() throws SchedulerException, InterruptedException {
        String executionTime = "2 10 10 10 OCT ?";
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id2");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);
        cronJobInfo.setSecondField("2");
        cronJobInfo.setMinutesField("10");
        cronJobInfo.setHourField("10");
        cronJobInfo.setDayField("10");
        cronJobInfo.setMonthField("10");
        String secondField = cronJobInfo.getSecondField();
        String minutesField = cronJobInfo.getMinutesField();
        String hoursField = cronJobInfo.getHourField();
        String daysField = cronJobInfo.getDayField();
        String monthField = cronJobInfo.getMonthField();
        trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        quartzTaskService.scheduleJobs(trigger, cronJobInfo, TaskHistory.class);
        List list = quartzTaskService.queryJob();
        assertEquals(list.get(0), "Test");
    }

    @Test
    public void getJobs() throws SchedulerException, InterruptedException {
        String executionTime = "2 10 10 10 OCT ?";
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id3");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);

        trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        quartzTaskService.scheduleJobs(trigger, cronJobInfo, TaskHistory.class);
        Set<JobDetailDTO> jobDetailDTOS = quartzTaskService.getJobs("Test", "id3");
        assertEquals(jobDetailDTOS.size(), 1);
        List list = new ArrayList(jobDetailDTOS);
        JobDetailDTO jobDetailDTO = (JobDetailDTO) list.get(0);
        assertEquals(jobDetailDTO.getKey().toString(), "Test.id3");
        assertNull(jobDetailDTO.getDescription());
        assertEquals(jobDetailDTO.getJobClass(), TaskHistory.class);
        assertFalse(jobDetailDTO.isDurability());
        JobDataMap jobDataMap = jobDetailDTO.getJobDataMap();
        assertNull(jobDataMap.get("id4"));
        assertFalse(jobDetailDTO.isShouldRecover());
        assertNull(jobDetailDTO.getStatus());

        SimpleJobInfo simpleJobInfo = new SimpleJobInfo();
        simpleJobInfo.setRate("1");
        simpleJobInfo.setTimes("1");
        simpleJobInfo.setJobName("id8");
        simpleJobInfo.setJobGroup("Test");
        simpleJobInfo.setJobType("TaskHistory");
        Date date = new Date();
        SimpleTrigger simpleTrigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger2", "Test")
                .startAt(date)
                .forJob("id8", "Test")
                .build();
        quartzTaskService.scheduleJobs(simpleTrigger, simpleJobInfo, TaskHistory.class);
        jobDetailDTOS = quartzTaskService.getJobs("Test", "id8");
        list = new ArrayList(jobDetailDTOS);
        jobDetailDTO = (JobDetailDTO) list.get(0);
        assertEquals(jobDetailDTO.getKey().toString(), "Test.id8");
    }

    @Test
    public void deleteJob() throws SchedulerException {
        quartzTaskService.deleteJob("Test", "id");
    }

    @Test
    public void resumeJob() throws SchedulerException, InterruptedException {
        String executionTime = "2 10 10 10 OCT ?";
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id4");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);

        trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        quartzTaskService.scheduleJobs(trigger, cronJobInfo, TaskHistory.class);
        quartzTaskService.resumeJob("Test", "id4");
    }

    @Test
    public void executeOnce() throws SchedulerException,InterruptedException {
        String executionTime = "2 10 10 10 OCT ?";
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id5");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);

        trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        quartzTaskService.scheduleJobs(trigger, cronJobInfo, TaskHistory.class);
        quartzTaskService.executeOnce("Test", "id5");
    }

    @Test
    public void pauseJob() throws SchedulerException, InterruptedException {
        String executionTime = "2 10 10 10 OCT ?";
        cronJobInfo.setJobGroup("Test");
        cronJobInfo.setJobName("id6");
        cronJobInfo.getJobData().put("campaignId", "id");
        cronJobInfo.getJobData().put("taskId", "id");
        cronJobInfo.setCronExpression(executionTime);

        trigger = QuartzUtil.getJobTrigger(cronJobInfo);
        quartzTaskService.scheduleJobs(trigger, cronJobInfo, TaskHistory.class);
        quartzTaskService.pauseJob("Test", "id6");
    }
}
