package com.genie.schedule.utils;

import com.genie.schedule.service.dto.CronJobInfo;
import com.genie.schedule.service.dto.SimpleJobInfo;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.spi.MutableTrigger;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzUtil {

    private QuartzUtil() {

    }

    public static Trigger getJobTrigger(CronJobInfo cronJobInfo) {
        MutableTrigger trigger = CronScheduleBuilder.cronSchedule(cronJobInfo.getCronExpression())
//            .inTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
            .build();
        trigger.setKey(new TriggerKey(cronJobInfo.getJobName(), cronJobInfo.getJobGroup()));
        return trigger;
    }


    private static Trigger getJobTrigger(SimpleJobInfo simpleJobInfo) {
        Integer rateInt = new Integer(simpleJobInfo.getRate());
        Integer timesInt = new Integer(simpleJobInfo.getTimes());
        return newTrigger()
            .withIdentity(simpleJobInfo.getJobName(), simpleJobInfo.getJobGroup())
            .withSchedule(simpleSchedule().withIntervalInMinutes(rateInt).withRepeatCount(timesInt))
            .build();
    }

}
