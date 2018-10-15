package com.genie.es.service;

import com.genie.data.filter.StringFilter;
import com.genie.es.entity.TaskHistory;
import com.genie.es.repository.TaskHistorySimpleReporistory;
import com.genie.es.service.dto.TaskHistoryCriteria;
import com.genie.es.util.EmbedSearchServer;
import com.genie.es.util.SpringESTestUtil;
import com.genie.es.util.TaskHistoryUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeValidationException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.UUID;

public class TaskHistoryQueryServiceTest {

    private ElasticsearchOperations elasticSearchOperations;

    private TaskHistoryQueryService taskHistoryQueryService;

    private TaskHistorySimpleReporistory taskHistoryReporistory;

    @Before
    public void setup() throws NodeValidationException {
        EmbedSearchServer server = new EmbedSearchServer();
        server.start();
        Client client = server.getClient();
        elasticSearchOperations = SpringESTestUtil.createElasticSearchOperations(client);
        taskHistoryReporistory = new TaskHistorySimpleReporistory(elasticSearchOperations);
        taskHistoryQueryService = new TaskHistoryQueryService(taskHistoryReporistory);
    }

    @Test
    public void findByCriteria() {
        TaskHistoryCriteria criteria = new TaskHistoryCriteria();
        StringFilter f = new StringFilter();
        f.setEquals("F_001");
        criteria.setFactId(f);
//        criteria.setTaskId("T_001");

        String json = "";
        taskHistoryQueryService.findByCriteria(criteria);
    }

    private void initData(){
        String id = UUID.randomUUID().toString();
        TaskHistory history = TaskHistoryUtil.createTaskHistory("camp_001");
        taskHistoryReporistory.save(history);
    }
}
