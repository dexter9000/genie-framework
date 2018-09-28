package com.genie.es.repository;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ShardingElasticSearchRepositoryTest {

    private TaskHistoryShardingElasticSearchReporistory repository;
    private static ElasticSearchOperations elasticSearchOperations;

    private static EmbedSearchServer server;
    private static Client client;

    @BeforeClass
    public static void init() throws NodeValidationException, UnknownHostException {
        server = new EmbedSearchServer();
        server.start();
        client = server.getClient();
        elasticSearchOperations = ESTestUtil.createElasticSearchOperations(client);
    }

    @AfterClass
    public static void closeEmbedSearchServer() throws IOException {
        server.stop();
    }

    @Before
    public void setup() {
        repository = new TaskHistoryShardingElasticSearchReporistory(elasticSearchOperations);
        try {
            elasticSearchOperations.removeIndex("task_history_camp_001");
            elasticSearchOperations.removeIndex("task_history_camp_003");
            elasticSearchOperations.removeIndex("task_history_camp_006");
            elasticSearchOperations.removeIndex("task_history_camp3_001");
            elasticSearchOperations.removeIndex("task_history_camp4_001");
        } catch (IndexNotFoundException e) {
            //
        }
    }


    @Test
    public void save() throws InterruptedException {
        TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_001");

        repository.save(taskHistory);

        Thread.sleep(3000);
        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_001");
        long size = elasticSearchOperations.count("task_history_camp_001", TaskHistory.class.getSimpleName(), queryBuilder);
        assertThat(size).isEqualTo(1);
    }

    @Test
    public void save100() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_001");
            repository.save(taskHistory);
        }

        Thread.sleep(2000);

        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_001");
        long size = elasticSearchOperations.count("task_history_camp_001", TaskHistory.class.getSimpleName(), queryBuilder);
        assertThat(size).isEqualTo(100);
    }

    @Test
    public void save15() throws InterruptedException {
        for (int i = 0; i < 15; i++) {
            TaskHistory taskHistory = createTaskHistory("camp_003");
            taskHistory.setTaskId(i);
            repository.save(taskHistory);
        }

        Thread.sleep(2000);
        Pageable pageable = new PageRequest(1,3);
        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_003");
        Page page = elasticSearchOperations.findPage("task_history_camp_003",
                TaskHistory.class.getSimpleName(),
                queryBuilder,
                pageable,
                TaskHistory.class);
        assertThat(page.getTotalElements()).isEqualTo(15);
        Sort sort = new Sort(Sort.Direction.DESC, "taskId");
        Pageable pageableSort = new PageRequest(1, 1, sort);
        QueryBuilder queryBuilderSort = QueryBuilders.termQuery("campaignId", "camp_003");
        Page pageSort = elasticSearchOperations.findPage("task_history_camp_003",
                TaskHistory.class.getSimpleName(),
                queryBuilder,
                pageableSort,
                TaskHistory.class);
        Thread.sleep(2000);
        assertThat(pageSort.getTotalElements()).isEqualTo(15);
    }

    @Test
    public void get() throws InterruptedException {
        TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_006");
        String id = UUID.randomUUID().toString();
        elasticSearchOperations.create("task_history_camp_006", TaskHistory.class.getSimpleName(), id, taskHistory);

        Thread.sleep(3000);

        TaskHistory taskHistory1 = repository.get("task_history_camp_006", id);
        int taskId = taskHistory1.getTaskId();
        assertThat(taskId).isEqualTo(1);
    }

    @Test
    public void saveCollection() throws InterruptedException {
        List<TaskHistory> taskHistories = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_001");
            taskHistories.add(taskHistory);
        }
        repository.save(taskHistories);

        Thread.sleep(5000);
        QueryBuilder queryBuilder = QueryBuilders.termQuery("campaignId", "camp_001");
        long size = elasticSearchOperations.count("task_history_camp_001", TaskHistory.class.getSimpleName(), queryBuilder);

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
