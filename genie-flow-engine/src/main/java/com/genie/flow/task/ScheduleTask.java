package com.genie.flow.task;

import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;

/**
 * 定时任务节点，不支持流式数据
 */
public class ScheduleTask extends BaseTask {


    ScheduleTask(FlowElement flow) {
        super(flow);
    }

    @Override
    public TaskResult executeTask() {
        return null;
    }
}
