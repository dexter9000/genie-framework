package com.genie.es.entity;

import com.genie.data.annotation.CriteriaEntity;
import com.genie.es.annotation.ESIndex;
import com.genie.es.annotation.ShardingId;
import org.springframework.data.annotation.Id;

@ESIndex(name = "task_history_", shard = true)
@CriteriaEntity(type = "")
public class TaskHistory {

    @Id
    private String factId;
    private int taskId;
    @ShardingId
    private String campaignId;
    private String metaId;
    private String result;
    private String status;
    private String startTime;
    private String endTime;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFactId() {
        return factId;
    }

    public void setFactId(String factId) {
        this.factId = factId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMetaId() {
        return metaId;
    }

    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }
}
