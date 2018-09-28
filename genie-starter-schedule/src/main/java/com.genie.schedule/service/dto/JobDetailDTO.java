package com.genie.schedule.service.dto;

import org.quartz.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by meng013 on 2017/11/7.
 */
public class JobDetailDTO implements Serializable {


    private String description;
    private Class<? extends Job> jobClass;
    private JobDataMap jobDataMap;
    private boolean durability = false;
    private boolean shouldRecover = false;
    private transient JobKey key = null;

    private Trigger.TriggerState status;
    private List<Trigger> triggers;

    public void setJobDetail(JobDetail jobDetail) {
        description = jobDetail.getDescription();
        jobClass = jobDetail.getJobClass();
        jobDataMap = jobDetail.getJobDataMap();
        durability = jobDetail.isDurable();
        shouldRecover = jobDetail.requestsRecovery();
        key = jobDetail.getKey();

    }

    public String getDescription() {
        return description;
    }

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public boolean isDurability() {
        return durability;
    }

    public boolean isShouldRecover() {
        return shouldRecover;
    }

    public JobKey getKey() {
        return key;
    }

    public Trigger.TriggerState getStatus() {
        return status;
    }

    public void setStatus(Trigger.TriggerState status) {
        this.status = status;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }
}
