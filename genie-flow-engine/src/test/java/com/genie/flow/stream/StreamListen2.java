package com.genie.flow.stream;

import com.genie.flow.domain.Fact;

public class StreamListen2 implements FactQueueListen {

    public void receiveFact(Fact fact){
        System.out.println("StreamListen2 receive: " + fact);
    }
}
