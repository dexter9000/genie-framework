package com.genie.es.repository;

import com.genie.es.entity.TaskHistory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

public class TaskHistorySimpleReporistory extends SimpleElasticsearchRepository<TaskHistory> {

    public TaskHistorySimpleReporistory(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }
}
