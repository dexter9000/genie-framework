package com.genie.schedule.config;

import com.genie.schedule.service.QuartzTaskService;
import com.genie.schedule.service.dto.CronJobInfo;
import com.genie.schedule.utils.QuartzUtil;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @ClassName: InitQuartJob
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled", havingValue = "true", matchIfMissing = true)
@EnableScheduling
@EnableConfigurationProperties({QuartzProperties.class})
public class QuartzAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(QuartzAutoConfiguration.class);

    @Autowired
    private QuartzProperties quartzProperties;
    @Autowired
    private JobFactory jobFactory;
    @Autowired
    private QuartzTaskService quartzTaskService;

    /**
     * 调度工厂bean
     *
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        log.info("Scheduler Start...");
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        //用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        factory.setSchedulerName("QuartzScheduler");
        //用于quartz集群,加载quartz数据源
        factory.setQuartzProperties(quartzProperties.getProperties());
        //QuartzScheduler 延时启动，应用启动完20秒后 QuartzScheduler 再启动
        factory.setStartupDelay(20);

        factory.setJobFactory(jobFactory);
        //注册触发器

        return factory;
    }

    @Bean
    public SchedulerFactory schedulerFactory() {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }

    /**
     * 自动从配置文件中加载定时任务
     * @throws SchedulerException
     */
    @Autowired(required = false)
    public void autoRegisterJobs() throws SchedulerException {
        QuartzProperties.Schedule[] schedules = quartzProperties.getSchedules();
        for (QuartzProperties.Schedule schedule : schedules) {
            long jobSize = quartzTaskService.getJobs(schedule.getGroup(), schedule.getName()).size();
            if (jobSize == 0) {
                log.info("Register {}", schedule.getName());
                CronJobInfo cronJobInfo = new CronJobInfo();
                cronJobInfo.setJobGroup(schedule.getGroup());
                cronJobInfo.setJobName(schedule.getName());
                cronJobInfo.setCronExpression(schedule.getCron());
                Trigger trigger = QuartzUtil.getJobTrigger(cronJobInfo);
                quartzTaskService.scheduleJobs(trigger, cronJobInfo, schedule.getClassName());
            } else {
                log.info("{} have been registed", schedule.getName());
            }
        }
    }
}
