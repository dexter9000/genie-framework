package com.genie.flow.stream;

import com.genie.flow.domain.Fact;
import com.genie.flow.service.FlowRepositoryService;
import com.genie.flow.task.StreamTask;

public class StreamListen {

    private FlowRepositoryService flowRepositoryService;

    public void receiveFact(Fact fact){
        StreamTask task = flowRepositoryService.getStreamTask(fact.getTaskId());
        task.executeStream(fact);
        
    }
}
