package com.genie.flow.task;

import com.genie.flow.domain.Fact;
import com.genie.flow.model.TaskResult;
import org.flowable.bpmn.model.FlowElement;

/**
 * 以流的方式接受数据，判断分支后以流的方式流转数据到下一个节点
 */
public class ConditionTask extends StreamTask {

    private DecisionTable decisionTable;

    /**
     * 是否立即判断
     */
    private boolean immed;



    ConditionTask(FlowElement flow) {
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
