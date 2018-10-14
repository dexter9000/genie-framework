package com.genie.schedule.service.dto;

import org.quartz.JobDataMap;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName: JobInfo
 */
public class JobInfo {

    //任务组名称
    @NotNull(message = "job group can not be null!")
    private String jobGroup;

    //任务名称
    @NotNull(message = "job name can not be null!")
    private String jobName;

    @NotNull(message = "job type can not be null!")
    private String jobType;

    private JobDataMap jobData = new JobDataMap();

    //参数名称
    private String[] argsNames;

    //参数值
    private String[] argsValues;

    private Date startTime;
    private Date endTime;

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public JobDataMap getJobData() {
        return jobData;
    }

    public void setJobData(JobDataMap jobData) {
        this.jobData = jobData;
    }

    public String[] getArgsNames() {
        return argsNames;
    }

    public void setArgsNames(String[] argsNames) {
        this.argsNames = argsNames;
    }

    public String[] getArgsValues() {
        return argsValues;
    }

    public void setArgsValues(String[] argsValues) {
        this.argsValues = argsValues;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public interface SaveValidate {

    }

    public interface DeleteValidate {

    }

}
