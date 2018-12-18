package com.genie.flow.model;


import com.genie.flow.enumeration.TaskType;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 2018年5月17日17:03:37
 * 活动流程对象
 */
@ToString
public class TaskNode implements Serializable {

    private String taskId;

    private String name;

    private List<RouterItem> routers;

    private TaskType type;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RouterItem> getRouters() {
        return routers;
    }

    public void setRouters(List<RouterItem> routers) {
        this.routers = routers;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
}
