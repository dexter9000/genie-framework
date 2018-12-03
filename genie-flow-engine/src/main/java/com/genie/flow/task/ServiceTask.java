package com.genie.flow.task;

import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;

public class ServiceTask extends BaseTask {

    private String result;

    public ServiceTask(FlowElement flow) {
        super(flow);
    }

    @Override
    public TaskResult executeTask() {
        return null;
    }
}
