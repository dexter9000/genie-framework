package com.genie.flow.model;

import com.genie.flow.TaskFactory;
import com.genie.flow.domain.Fact;
import com.genie.flow.service.TaskCache;
import com.genie.flow.task.BaseTask;
import com.genie.flow.task.StartTask;
import com.genie.flow.task.StreamTask;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProcessInstance {

    private static final Logger log = LoggerFactory.getLogger(ProcessInstance.class);

    private Process process;
    private TaskFactory taskFactory;

    public ProcessInstance(Process process) {
        this.process = process;
    }

    public void init() {
        log.info("init process {}:{}", process.getName(), process.getId());
        // 得到所有的流程节点
        process.getFlowElements().stream()
            .filter(flow -> flow instanceof FlowNode)
            .map(flow -> (FlowNode) flow)
            .forEach(flowNode -> TaskCache.addTask(TaskFactory.createTask(flowNode)));
    }

    /**
     * 主体流程调度
     */
    public void startProcess() {
        FlowElement flowElement = process.findFlowElementsOfType(StartEvent.class).get(0);

        StartTask task = new StartTask(flowElement);
        while (true) {
            TaskResult result = task.executeTask();

            if (result.isDirectNext()) {
                // 直接执行下一步
                List<SequenceFlow> outgoings = ((FlowNode) flowElement).getOutgoingFlows();
                if (outgoings.size() > 1) {
                    for (SequenceFlow outgoing : outgoings) {
                        log.debug("Out Sequence : {}", outgoing.getName());

                    }
                }
            }
        }

    }

    public void getTask(String taskId){
        FlowElement flowEl = process.getFlowElement(taskId);
        flowEl.getAttributes();

        List<SequenceFlow> flows = ((FlowNode) process.getFlowElement(taskId)).getOutgoingFlows();


        for(SequenceFlow flow : flows){

            flow.getTargetRef();
        }
    }

    /**
     * 数据流调度
     */
    public void executeStream(Fact fact) {
        BaseTask task = TaskCache.getTask(fact.getTaskId());
        if (task instanceof StreamTask) {
            ((StreamTask) task).executeStream(fact);
        }

//        task.get
    }
}
