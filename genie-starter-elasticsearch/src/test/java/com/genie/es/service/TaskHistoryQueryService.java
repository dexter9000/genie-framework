package com.genie.es.service;

import com.genie.es.entity.TaskHistory;
import com.genie.es.entity.TaskHistory_;
import com.genie.es.repository.TaskHistorySimpleReporistory;
import com.genie.es.service.dto.TaskHistoryCriteria;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskHistoryQueryService extends QueryService<TaskHistory>{

    private final Logger log = LoggerFactory.getLogger(TaskHistoryQueryService.class);

    private final TaskHistorySimpleReporistory taskHistoryRepository;

    public TaskHistoryQueryService(TaskHistorySimpleReporistory taskHistoryRepository) {
        this.taskHistoryRepository = taskHistoryRepository;
    }


    public Iterable<TaskHistory> findByCriteria(TaskHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryBuilder query = createQuery(criteria);
        return taskHistoryRepository.search(query);
    }

    private QueryBuilder createQuery(TaskHistoryCriteria criteria) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (criteria != null) {
            if (criteria.getFactId() != null) {
                boolQueryBuilder.must(buildStringQuery(criteria.getFactId(), TaskHistory_.factId));
            }

        }
        return boolQueryBuilder;
    }
}
