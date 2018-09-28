package com.genie.schedule.service.dto;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.TimeZone;

/**
 * Created by meng013 on 2017/11/7.
 */
public class CronTriggerDTO extends AbstractTriggerDTO {

    private CronExpression cronEx = null;
    private TimeZone timeZone = null;


    public void setCronExpression(String cronExpression) {
        TimeZone origTz = getTimeZone();
        try {
            this.cronEx = new CronExpression(cronExpression);
            this.cronEx.setTimeZone(origTz);
        } catch (ParseException e) {
            // None
        }
    }

    public String getCronExpression() {
        return cronEx == null ? null : cronEx.getCronExpression();
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
