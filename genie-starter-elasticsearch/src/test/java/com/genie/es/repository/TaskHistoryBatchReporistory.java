package com.genie.es.repository;

import com.genie.es.config.ElasticSearchProperties;

public class TaskHistoryBatchReporistory<TaskHistory> extends BatchShardingRepository<TaskHistory> {

    public TaskHistoryBatchReporistory(ElasticSearchOperations elasticSearchOperations, ElasticSearchProperties elasticSearchProperties) {
        super(elasticSearchOperations, elasticSearchProperties);
    }
}
