package com.genie.flow.task;

import com.genie.flow.domain.Fact;
import org.flowable.bpmn.model.FlowElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流式任务
 */
public abstract class StreamTask extends BaseTask {

    private static final Logger log = LoggerFactory.getLogger(StreamTask.class);

    protected boolean realtime;

    StreamTask(FlowElement flow) {
        super(flow);
    }

    public void executeStream(Fact fact){
        beforeStream();

        afterStream(fact);
    }

    /**
     * 基于触发条件判断任务是否执行
     */
    public void beforeStream() {
        log.debug("beforeStream");
        // TODO 记录任务状态
        // TODO 任务条件
    }

    /**
     * 任务后执行
     */
    public Fact afterStream(Fact fact) {
        log.debug("afterStream");
        // 任务结果统计
        return fact;
    }

}
