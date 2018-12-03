package com.genie.flow.task;

import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTask {

    private static final Logger log = LoggerFactory.getLogger(BaseTask.class);

    protected String id;
    protected String name;
    protected String flowId;

    BaseTask(FlowElement flow) {
        this.id = flow.getId();
        this.name = flow.getName();

    }

    public TaskResult execute(){
        log.debug("executeTask Task {}:{}", name, id);
        return executeTask();
    }

    public abstract TaskResult executeTask();

    public void init(){

    }

    public void beforeTask(){

    }

    public void afterTask(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
