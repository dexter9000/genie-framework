package com.genie.flow.model;


import com.genie.flow.enumeration.CommonStatus;
import lombok.ToString;

/**
 * 2018年5月29日16:36:36
 * 活动流程对象
 */
@ToString
public class FlowModel {

    private String id;

    private String tasks;

    private String timestamp;

    private CommonStatus status;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
