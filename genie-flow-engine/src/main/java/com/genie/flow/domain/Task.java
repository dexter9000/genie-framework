package com.genie.flow.domain;

import com.genie.flow.enumeration.TaskStatus;
import com.genie.flow.enumeration.TaskType;
import lombok.ToString;

import java.time.ZonedDateTime;

@ToString
public class Task {


    protected String id;
    /**
     * 活动ID
     */
    protected String campaignId;
    /**
     * 任务标题
     */
    protected String title;
    protected String summary;
    /**
     * 任务类型
     */
    protected TaskType type;
    protected TaskStatus status;
    /**
     * 是否定时
     */
    protected boolean scheduled;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;
    /**
     * 下一个任务，普通任务时的下一个任务，规则任务的默认下一个任务
     */
    protected String nextTaskId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(String nextTaskId) {
        this.nextTaskId = nextTaskId;
    }

}
