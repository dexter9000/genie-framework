package com.genie.flow.stream;

import com.genie.flow.domain.Fact;
import com.genie.flow.service.FlowRepositoryService;
import com.genie.flow.task.StreamTask;

public class StreamListen implements FactQueueListen {

    private FlowRepositoryService flowRepositoryService;

    public void receiveFact(Fact fact){
        StreamTask task = flowRepositoryService.getStreamTask(fact.getId(), fact.getTaskId());
        task.executeStream(fact);
        
    }
}
