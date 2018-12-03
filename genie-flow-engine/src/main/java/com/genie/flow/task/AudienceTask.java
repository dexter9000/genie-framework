package com.genie.flow.task;

import com.genie.flow.model.TaskResult;
import com.genie.flow.domain.Fact;
import org.flowable.bpmn.model.FlowElement;

public class AudienceTask extends BaseTask {

    public AudienceTask(FlowElement flow) {
        super(flow);
    }

    @Override
    public TaskResult executeTask() {

        TaskResult result = new TaskResult();
//        result.
        return result ;
    }

    public void executeStream(Fact fact){

    }


}
