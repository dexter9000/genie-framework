package com.genie.flow.process;

import com.genie.flow.model.TaskNode;
import com.genie.flow.task.BaseTask;
import com.genie.flow.task.StartTask;

public class StartTaskProcess extends BaseTaskProcess {

    private StartTask task;

    public StartTaskProcess(BaseTask task, TaskNode taskNode) {
        super(task, taskNode);
    }

    @Override
    public String getNextTaskId() {
        return null;
    }

    @Override
    public BaseTask getTask() {
        return null;
    }
}
