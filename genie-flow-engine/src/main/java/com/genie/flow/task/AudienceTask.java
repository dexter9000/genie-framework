package com.genie.flow.task;

import com.genie.flow.model.TaskResult;
import com.genie.flow.domain.Fact;
import org.flowable.bpmn.model.FlowElement;

/**
 * 根据条件查询数据并以流的方式流转到下一个节点，本身不能接受数据
 */
public class AudienceTask extends StreamTask {

    public AudienceTask(FlowElement flow) {
        super(flow);
    }

    @Override
    public TaskResult executeTask() {
        // 流程启动时或者执行到该步时发送请求
        TaskResult result = new TaskResult();
//        result.
        return result ;
    }

    public void executeStream(Fact fact){
        // 接收到数据时
    }


}
