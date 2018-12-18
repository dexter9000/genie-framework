package com.genie.flow.task;

import com.genie.flow.domain.Fact;
import com.genie.flow.enumeration.TaskStatus;
import com.genie.flow.enumeration.TaskType;
import com.genie.flow.model.FactResultDTO;
import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public abstract class BaseTask {

    private static final Logger log = LoggerFactory.getLogger(BaseTask.class);

    protected String id;
    protected String name;
    protected String flowId;
    protected TaskType type;
    protected LocalDate startTime;
    protected LocalDate endTime;
    protected String summary;
    protected TaskStatus status;
    protected FlowNode flow;

    BaseTask(FlowElement flow) {
        this.id = flow.getId();
        this.name = flow.getName();

    }

    /**
     * 执行带参数的任务
     * @param fact
     * @return
     */
    public Fact execute(Fact fact){
        log.debug("executeTask Task {}:{}", name, id);
        // TODO
        return null;
    }

    public TaskResult execute(){
        log.debug("executeTask Task {}:{}", name, id);
        return executeTask();
    }

    /**
     * 执行任务
     * @return
     */
    public abstract TaskResult executeTask();

    public void init(){

    }

    public void beforeTask(){

    }

    public void afterTask(){

    }

    public String nextTask(){
        List<SequenceFlow> outgoings = flow.getOutgoingFlows();
        // TODO 逻辑未实现
        return outgoings.get(0).getTargetRef();
    }

    public String nextTask(FactResultDTO factResult){
        List<SequenceFlow> outgoings = flow.getOutgoingFlows();
        // TODO 逻辑未实现
        return outgoings.get(0).getTargetRef();
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

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
