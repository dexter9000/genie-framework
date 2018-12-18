package com.genie.schedule.service;

import com.genie.schedule.service.dto.CronJobInfo;
import com.genie.schedule.service.dto.JobDetailDTO;
import com.genie.schedule.service.dto.JobInfo;
import com.genie.schedule.service.dto.SimpleJobInfo;
import com.genie.schedule.service.mapper.CronTriggerMapper;
import com.genie.schedule.service.mapper.SimpleTriggerMapper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.MutableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class QuartzTaskService {

    private static final Logger log = LoggerFactory.getLogger(QuartzTaskService.class);

    @Autowired(required = false)
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private CronTriggerMapper cronTriggerMapper;
    @Autowired
    private SimpleTriggerMapper simpleTriggerMapper;

    @Value("timeZone")
    private String timeZone = "Asia/Shanghai";

    /**
     * 开启一个新的任务服务
     *
     * @param jobInfo
     * @throws SchedulerException
     */
    @Transactional
    public void scheduleSimpleJobs(SimpleJobInfo jobInfo, Class jobClass) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobDetail job = newJob(jobClass)
            .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
            .setJobData(jobInfo.getJobData())
            .build();

        Integer rateInt = new Integer(jobInfo.getRate());
        Integer timesInt = new Integer(jobInfo.getTimes());
        Trigger trigger = newTrigger()
            .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
            .withSchedule(simpleSchedule().withIntervalInMinutes(rateInt).withRepeatCount(timesInt))
            .build();

        scheduler.scheduleJob(job, trigger);
    }
    /**
     * 开启一个新的任务服务
     *
     * @param jobInfo
     * @throws SchedulerException
     */
    @Transactional
    public void scheduleCronJobs(CronJobInfo jobInfo, Class jobClass) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobDetail job = newJob(jobClass)
            .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
            .setJobData(jobInfo.getJobData())
            .build();

        String tz = StringUtils.isNoneBlank(jobInfo.getTimeZone())? jobInfo.getTimeZone() : timeZone;
        MutableTrigger trigger = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression())
            .inTimeZone(TimeZone.getTimeZone(tz))
            .build();
        trigger.setKey(new TriggerKey(jobInfo.getJobName(), jobInfo.getJobGroup()));

        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 开启一个新的任务服务
     *
     * @param trigger
     * @param jobInfo
     * @throws SchedulerException
     */
    @Transactional
    public void scheduleJobs(Trigger trigger, JobInfo jobInfo, Class jobClass) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobDetail job = newJob(jobClass)
            .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
            .setJobData(jobInfo.getJobData())
            .build();

        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 重置一个任务
     *
     * @param trigger
     * @param jobInfo
     * @throws SchedulerException
     * @Title: rescheduleJob
     * @Description:
     * @return: void
     */
    @Transactional
    public void rescheduleJob(Trigger trigger, JobInfo jobInfo, Class jobClass) throws SchedulerException {
        deleteJob(jobInfo.getJobGroup(), jobInfo.getJobName());
        scheduleJobs(trigger, jobInfo, jobClass);
    }

    public List<String> queryJob() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<String> groupnames = null;
        try {
            groupnames = scheduler.getJobGroupNames();
        } catch (SchedulerException e) {
            log.error("Query job failed!", e);
        }
        return groupnames;
    }

    /**
     * 根据group和name查询任务集合
     *
     * @param group
     * @param name
     * @return
     */
    public Set<JobDetailDTO> getJobs(String group, String name) {
        Set<JobDetailDTO> jobSet = new HashSet<>();
        Set<JobKey> jobKeys = null;
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            if (StringUtils.isEmpty(group)) {
                jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            } else {
                jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(group));
            }
        } catch (SchedulerException e) {
            log.error("Find Job error.", e);
        }
        for (JobKey jobKey : jobKeys) {
            if (StringUtils.isNotEmpty(name) && !jobKey.toString().contains(name)) {
                continue;
            }
            jobSet.add(parseJobDetail(scheduler, jobKey));
        }
        return jobSet;
    }

    /**
     * @param scheduler
     * @param jobKey
     * @return
     */
    private JobDetailDTO parseJobDetail(Scheduler scheduler, JobKey jobKey) {
        JobDetailDTO jobDetailDTO = new JobDetailDTO();
        try {
            List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
            List triggerDTOs = triggers.stream().map(trigger -> {
                if (trigger instanceof CronTriggerImpl) {
                    return cronTriggerMapper.toDto((CronTriggerImpl) trigger);
                } else if (trigger instanceof SimpleTriggerImpl) {
                    return simpleTriggerMapper.toDto((SimpleTriggerImpl) trigger);
                } else {
                    return trigger;
                }
            }).collect(Collectors.toList());
            jobDetailDTO.setJobDetail(scheduler.getJobDetail(jobKey));
            jobDetailDTO.setTriggers(triggerDTOs);
        } catch (SchedulerException e) {
            log.error("Build Job Info error.", e);
        }
        return jobDetailDTO;
    }

    /**
     * 删除任务服务
     *
     * @param group
     * @param name
     * @throws SchedulerException
     * @return: void
     */
    @Transactional
    public void deleteJob(String group, String name) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.deleteJob(JobKey.jobKey(name, group));
    }

    /**
     * 恢复任务
     *
     * @param jobGroup
     * @param jobName
     * @throws SchedulerException
     */
    @Transactional
    public void resumeJob(String jobGroup, String jobName) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 执行一次服务
     *
     * @param jobGroup
     * @param jobName
     * @throws SchedulerException
     */
    public void executeOnce(String jobGroup, String jobName) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Trigger trigger = newTrigger().
            withIdentity(jobName + UUID.randomUUID().toString(), jobGroup).
            withPriority(100).
            forJob(JobKey.jobKey(jobName, jobGroup)).build();
        scheduler.scheduleJob(trigger);
    }

    /**
     * 暂停任务服务
     *
     * @param jobGroup
     * @param jobName
     * @throws SchedulerException
     */
    @Transactional
    public void pauseJob(String jobGroup, String jobName) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
    }


}
