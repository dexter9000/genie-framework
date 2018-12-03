package com.genie.flow.task;

import com.genie.flow.domain.Fact;
import org.flowable.bpmn.model.FlowElement;

/**
 * 流式任务
 */
public abstract class StreamTask extends BaseTask {


    StreamTask(FlowElement flow) {
        super(flow);
    }

    public abstract void executeStream(Fact fact);
}
