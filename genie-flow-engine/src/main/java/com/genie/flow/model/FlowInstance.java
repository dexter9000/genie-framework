package com.genie.flow.model;

import com.genie.flow.process.BaseTaskProcess;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程实例
 */
@ToString
public class FlowInstance {

    private String id;

    private String modelId;

    private String status; // TODO 枚举

    private String name;

    private String profile; // TODO 运行模式

    private Date startTime;

    private boolean completeable;

    private Map<String, Object> variables = new HashMap<>();

    private List<BaseTaskProcess> tasks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isCompleteable() {
        return completeable;
    }

    public void setCompleteable(boolean completeable) {
        this.completeable = completeable;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public List<BaseTaskProcess> getTasks() {
        return tasks;
    }

    public void setTasks(List<BaseTaskProcess> tasks) {
        this.tasks = tasks;
    }
}
