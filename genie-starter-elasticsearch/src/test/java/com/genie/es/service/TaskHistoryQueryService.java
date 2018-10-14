package com.genie.es.service;

import com.genie.es.entity.TaskHistory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

public class TaskHistoryQueryService extends QueryService<TaskHistory>{

    public List<TaskHistory> findByCriteria() {
        return null;
    }


    private QueryBuilder createSpecification() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must();

        return null;
    }
}
