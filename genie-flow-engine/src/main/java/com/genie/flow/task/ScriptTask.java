package com.genie.flow.task;

import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;

/**
 * 脚本任务，根据脚本执行，不支持流式数据
 */
public class ScriptTask extends BaseTask {

    private String result;

    public ScriptTask(FlowElement flow) {
        super(flow);
    }

    @Override
    public TaskResult executeTask() {
        return null;
    }
}
