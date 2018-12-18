package com.genie.flow.stream;

import com.genie.flow.domain.Fact;

public class StreamListen1 implements FactQueueListen {

    public void receiveFact(Fact fact){
        System.out.println("StreamListen1 receive: " + fact);
    }
}
