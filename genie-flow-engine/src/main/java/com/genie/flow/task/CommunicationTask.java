package com.genie.flow.task;

import com.genie.flow.domain.Fact;
import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;

/**
 * 以流的方式接受数据，处理完后再以流的方式流程数据到下一个节点
 */
public class CommunicationTask extends StreamTask {

    CommunicationTask(FlowElement flow) {
        super(flow);
    }

    @Override
    public void executeStream(Fact fact) {

    }

    @Override
    public TaskResult executeTask() {
        return null;
    }
}
