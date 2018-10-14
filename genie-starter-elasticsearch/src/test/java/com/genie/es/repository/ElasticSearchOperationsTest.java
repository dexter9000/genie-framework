package com.genie.es.repository;

import com.alibaba.fastjson.JSONObject;
import com.genie.es.entity.MappingProperty;
import com.genie.es.entity.Profile;
import com.genie.es.entity.TaskHistory;
import com.genie.es.util.ESTestUtil;
import com.genie.es.util.EmbedSearchServer;
import com.genie.es.util.TaskHistoryUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeValidationException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElasticSearchOperationsTest {

    private static final String INDEX_1 = "task_history_camp_001";
    private static final String TYPE_1 = "TaskHistory";
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
        elasticSearchOperations = ESTestUtil.createElasticSearchOperations(client);
    }

    @Test
    public void createAndRefresh() {
        String id = UUID.randomUUID().toString();
        TaskHistory history = TaskHistoryUtil.createTaskHistory("camp_001");
        elasticSearchOperations.createAndRefresh(INDEX_1, TYPE_1, id, history);
    }

    @Test
    public void update() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        TaskHistory history = TaskHistoryUtil.createTaskHistory("camp_001");
        elasticSearchOperations.createAndRefresh(INDEX_1, TYPE_1, id, history);

        Thread.sleep(5000);
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//        Sort sort = new Sort("metaId");
//        Pageable pageable = new PageRequest(0, 10, sort);
//        elasticSearchOperations.findPage(INDEX_1, TYPE_1, queryBuilder, pageable, TaskHistory.class);
        Map<String, Object> maps = new HashMap<>();
        elasticSearchOperations.update(INDEX_1, TYPE_1, id, maps);

    }

    @Test
    public void testProfile() {
        Profile profile = new Profile();
        profile.setMemberId("123");
        profile.setMarketingProgram("456");
        profile.setCreatedTime(new Date());
        profile.setModifiedTime(ZonedDateTime.now());
        String str = JSONObject.toJSONString(profile);
        System.out.println(str);

    }

    @Test
    public void testGetMapping() {

        String id = UUID.randomUUID().toString();
        TaskHistory history = TaskHistoryUtil.createTaskHistory("camp_001");
        elasticSearchOperations.createAndRefresh(INDEX_1, TYPE_1, id, history);

        String mapping = elasticSearchOperations.getMapping(INDEX_1, TYPE_1);
        System.out.println(mapping);
    }

    @Test
    public void testUpdateMapping() {
        String mapping = elasticSearchOperations.getMapping(INDEX_1, TYPE_1);
        System.out.println(mapping);

        Map<String, MappingProperty> properties = new HashMap<>();
        properties.put("field1", new MappingProperty("keyword"));
        elasticSearchOperations.updateMapping(INDEX_1, TYPE_1, properties);

        mapping = elasticSearchOperations.getMapping(INDEX_1, TYPE_1);
        System.out.println(mapping);

    }
}
