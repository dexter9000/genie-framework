package com.genie.es.util;

import com.genie.es.repository.ElasticSearchOperations;
import com.genie.es.entity.TaskHistory;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeValidationException;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.UUID;

public class EmbedSearchServerTest {

    @Test
    public void test() throws NodeValidationException, UnknownHostException, InterruptedException {
        EmbedSearchServer server = new EmbedSearchServer();
        server.start();
        Client client = server.getClient();
        ElasticSearchOperations elasticSearchOperations = ESTestUtil.createElasticSearchOperations(client);
        elasticSearchOperations.removeIndex("task_history_");
        TaskHistory history = new TaskHistory();
        history.setCampaignId("camp_001");
        history.setTaskId(1);
        history.setFactId(UUID.randomUUID().toString());

        String id = UUID.randomUUID().toString();
        elasticSearchOperations.create("task_history_tt", TaskHistory.class.getSimpleName(), id, history);

        Thread.sleep(3000);
        String result = elasticSearchOperations.get("task_history_tt", TaskHistory.class.getSimpleName(), id);
        System.out.println(result);
    }
}
