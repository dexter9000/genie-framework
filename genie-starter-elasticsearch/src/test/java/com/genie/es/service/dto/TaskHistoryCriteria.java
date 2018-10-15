package com.genie.es.service.dto;

import com.genie.data.filter.IntegerFilter;
import com.genie.data.filter.StringFilter;

import java.io.Serializable;

public class TaskHistoryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private StringFilter factId;
    private IntegerFilter taskId;
    private StringFilter campaignId;
    private StringFilter metaId;
    private StringFilter result;
    private StringFilter status;
    private StringFilter startTime;
    private StringFilter endTime;

    public TaskHistoryCriteria() {
    }

    public StringFilter getFactId() {
        return factId;
    }

    public void setFactId(StringFilter factId) {
        this.factId = factId;
    }

    public IntegerFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(IntegerFilter taskId) {
        this.taskId = taskId;
    }

    public StringFilter getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(StringFilter campaignId) {
        this.campaignId = campaignId;
    }

    public StringFilter getMetaId() {
        return metaId;
    }

    public void setMetaId(StringFilter metaId) {
        this.metaId = metaId;
    }

    public StringFilter getResult() {
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(StringFilter startTime) {
        this.startTime = startTime;
    }

    public StringFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(StringFilter endTime) {
        this.endTime = endTime;
    }
}
