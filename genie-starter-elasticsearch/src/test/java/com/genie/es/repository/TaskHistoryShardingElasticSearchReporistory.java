package com.genie.es.repository;

import com.genie.es.entity.TaskHistory;

public class TaskHistoryShardingElasticSearchReporistory extends ShardingElasticSearchRepository<TaskHistory> {

    public TaskHistoryShardingElasticSearchReporistory(ElasticSearchOperations elasticSearchOperations) {
        super(elasticSearchOperations);
    }
}
