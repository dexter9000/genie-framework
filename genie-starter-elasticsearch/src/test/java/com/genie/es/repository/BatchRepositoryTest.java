package com.genie.es.repository;

import com.genie.es.config.ElasticSearchProperties;
import com.genie.es.entity.TaskHistory;
import com.genie.es.util.ESTestUtil;
import com.genie.es.util.EmbedSearchServer;
import com.genie.es.util.TaskHistoryUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.NodeValidationException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchRepositoryTest {

    private TaskHistoryBatchReporistory taskHistoryBatchReporistory;
    private ElasticSearchOperations elasticSearchOperations;

    private static EmbedSearchServer server;
    private static Client client;

    @BeforeClass
    public static void startEmbedSearchServer() throws NodeValidationException {
        server = new EmbedSearchServer();
        server.start();
        client = server.getClient();
    }

    @AfterClass
    public static void closeEmbedSearchServer() throws IOException {
        server.stop();
    }

    @Before
    public void setup() throws UnknownHostException {
        ElasticSearchProperties elasticSearchProperties = ESTestUtil.createElasticSearchProperties();

        elasticSearchOperations = ESTestUtil.createElasticSearchOperations(client);

        taskHistoryBatchReporistory = new TaskHistoryBatchReporistory(elasticSearchOperations, elasticSearchProperties);
        elasticSearchOperations.removeIndex("task_history_camp_001");
        elasticSearchOperations.removeIndex("task_history_camp2_001");
        elasticSearchOperations.removeIndex("task_history_camp2_002");
    }

    @Test
    public void saveSingle() throws InterruptedException {
        TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_001");
        taskHistoryBatchReporistory.save(taskHistory);

        Thread.sleep(30000);

        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_001");
        long size = elasticSearchOperations.count("task_history_camp_001",
                "TaskHistory",
                queryBuilder);
        assertThat(size).isEqualTo(1);
    }

    @Test
    public void save100() throws InterruptedException {
        for (int i = 0; i < 200; i++) {
            TaskHistory history = TaskHistoryUtil.createTaskHistory("camp2_00" + ((i % 2) +1));
            taskHistoryBatchReporistory.save(history);
        }
        Thread.sleep(10000);

        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp2_001");
        long size = elasticSearchOperations.count("task_history_camp2_001", "TaskHistory", queryBuilder);
        assertThat(size).isEqualTo(100);

        queryBuilder = QueryBuilders.termQuery("campaignId", "camp2_002");
        size = elasticSearchOperations.count("task_history_camp2_002", "TaskHistory", queryBuilder);
        assertThat(size).isEqualTo(100);
    }

}
