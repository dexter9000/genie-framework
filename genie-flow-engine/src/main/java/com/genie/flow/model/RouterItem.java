package com.genie.flow.model;

public class RouterItem {

    private String key;

    private String taskId;

    public RouterItem() {
    }

    public RouterItem(String key, String taskId) {
        this.key = key;
        this.taskId = taskId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
