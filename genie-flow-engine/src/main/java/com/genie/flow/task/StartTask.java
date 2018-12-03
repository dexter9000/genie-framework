package com.genie.flow.task;

import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartTask extends BaseTask {

    private static final Logger log = LoggerFactory.getLogger(StartTask.class);

    public StartTask(FlowElement flow) {
        super(flow);
    }

    @Override
    public TaskResult executeTask() {
        log.debug("executeTask StartTask for {}:{}", name, id);
        TaskResult result = new TaskResult();
        result.setResult("");
        return result;
    }
}
