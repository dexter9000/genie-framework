package com.genie.flow.stream;

import com.genie.flow.domain.Fact;
import org.junit.Test;

import static org.junit.Assert.*;

public class FactQueueTest {

    @Test
    public void test() throws InterruptedException {
        FactQueue factQueue = new FactQueue();

        String CHANNEL_1 = "CHANNEL_1";
        String CHANNEL_2 = "CHANNEL_2";

        Fact fact = new Fact();
        fact.setId("001");
        fact.setMetaId("1");
        fact.setMetadata("{'key':'100'}");

        factQueue.sendMessage(CHANNEL_1, fact);

        StreamListen1 listen1 = new StreamListen1();
        factQueue.registry(CHANNEL_1, listen1);

        StreamListen1 listen2 = new StreamListen1();
        factQueue.registry(CHANNEL_2, listen2);

        Thread.sleep(1000);
        factQueue.sendMessage(CHANNEL_2, fact);

    }
}
