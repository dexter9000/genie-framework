package com.genie.flow.stream;

import com.genie.flow.domain.Fact;

public interface FactQueueListen {

    void receiveFact(Fact fact);
}
