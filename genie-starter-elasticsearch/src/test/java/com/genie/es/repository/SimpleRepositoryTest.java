package com.genie.es.repository;

import com.genie.es.config.ElasticSearchProperties;
import com.genie.es.entity.TaskHistory;
import com.genie.es.util.ESTestUtil;
import com.genie.es.util.EmbedSearchServer;
import com.genie.es.util.TaskHistoryUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.NodeValidationException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleRepositoryTest {

    private TaskHistorySimpleReporistory repository;
    private static ElasticsearchTemplate elasticSearchOperations;

    private static ElasticSearchProperties elasticSearchProperties;
    private static EmbedSearchServer server;
    private static Client client;

    @BeforeClass
    public static void init() throws NodeValidationException, UnknownHostException {
        elasticSearchProperties = ESTestUtil.createElasticSearchProperties();
        server = new EmbedSearchServer();
        server.start();
        client = server.getClient();
        elasticSearchOperations = ESTestUtil.createElasticsearchTemplate(client);
    }

    @AfterClass
    public static void closeEmbedSearchServer() throws IOException {
        server.stop();
    }

    @Before
    public void setup()  {
        repository = new TaskHistorySimpleReporistory(elasticSearchOperations);
        try {
            elasticSearchOperations.deleteIndex("task_history_");
        } catch (IndexNotFoundException e) {
            //
        }

    }


    @Test
    public void save() throws InterruptedException {
        TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_005");

        repository.save(taskHistory);

        Thread.sleep(3000);
        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_005");

        SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
        long size = elasticSearchOperations.count(searchQuery);
        assertThat(size).isEqualTo(1);
    }

    @Test
    public void save100() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_002");
            repository.save(taskHistory);
        }

        Thread.sleep(2000);

        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_002");
        SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
        long size = elasticSearchOperations.count(searchQuery);
        assertThat(size).isEqualTo(100);
    }

    @Test
    public void saveCollection() throws InterruptedException {
        List<TaskHistory> taskHistories = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            TaskHistory taskHistory = createTaskHistory("camp_001");
            taskHistories.add(taskHistory);
        }
        repository.save(taskHistories);

        Thread.sleep(5000);
        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_001");
        SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);

        long size = elasticSearchOperations.count(searchQuery);

        assertThat(size).isEqualTo(100);
    }

    private TaskHistory createTaskHistory(String campaignId) {
        TaskHistory history = new TaskHistory();
        history.setCampaignId(campaignId);
        history.setTaskId(1);
        history.setFactId(UUID.randomUUID().toString());
        return history;
    }
}
