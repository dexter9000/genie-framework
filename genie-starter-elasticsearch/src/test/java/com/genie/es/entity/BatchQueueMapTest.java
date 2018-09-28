package com.genie.es.entity;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BatchQueueMapTest {

    @Test
    public void test() {
        BatchQueueMap queueMap = new BatchQueueMap(3);

        queueMap.add("001", createMember("Alex", 10));
        queueMap.add("001", createMember("Judy", 10));
        queueMap.add("002", createMember("Ruby", 10));

        assertFalse(queueMap.haveFullQueue());
        assertThat(queueMap.getFullQueue()).isNull();
        assertThat(queueMap.getQueue("001")).isNotNull();
        assertThat(queueMap.getQueue("003")).isNull();
        assertThat(queueMap.pop("002").size()).isEqualTo(1);

        queueMap.add("001", createMember("Yoyo", 10));
        assertTrue(queueMap.haveFullQueue());
        assertThat(queueMap.getFullQueue()).isNotNull();

        queueMap.add("001", createMember("Nicole", 10));
        assertThat(queueMap.pop("001").size()).isEqualTo(3);


    }

    private Member createMember(String name, long age) {
        Member member = new Member(UUID.randomUUID().toString(), name);
        member.setName(name);
        member.setAge(age);
        return member;
    }
}
