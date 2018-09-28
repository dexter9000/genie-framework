package com.genie.es.repository;

import com.genie.es.entity.TaskHistory;

public class TaskHistorySimpleReporistory extends SimpleElasticSearchRepository<TaskHistory> {

    public TaskHistorySimpleReporistory(ElasticSearchOperations elasticSearchOperations) {
        super(elasticSearchOperations);
    }
}
