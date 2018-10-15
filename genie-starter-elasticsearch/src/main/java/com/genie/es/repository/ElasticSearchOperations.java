package com.genie.es.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.genie.es.entity.MappingProperty;
import com.genie.es.exception.ElasticSearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearch基本处理类
 */
@SuppressWarnings({"unchecked", "Duplicates"})
public class ElasticSearchOperations {

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchOperations.class);

    protected Client client;

    public ElasticSearchOperations(Client client) {
        this.client = client;
    }


    /**
     * 根据索引和类型查询mapping结构
     *
     * @param index 索引名
     * @param type  类型名
     * @return json结构的mapping结构
     */
    public String getMapping(String index, String type) {
        return client.admin().cluster().prepareState().execute().actionGet()
            .getState().getMetaData().getIndices()
            .get(index).getMappings().get(type).source().toString();
    }

    /**
     * 更新mapping结构，由于ElasticSearch不支持已存在字段的修改，所以此处是对mapping结构新增索引字段
     *
     * @param index      索引名
     * @param type       类型
     * @param properties 新增字段集合
     */
    public void updateMapping(String index, String type, Map<String, MappingProperty> properties) {

        JSONObject jsonProperties = new JSONObject();
        jsonProperties.put("properties", properties);

        JSONObject json = new JSONObject();
        json.put(type, jsonProperties);
        PutMappingRequest mappingRequest = Requests.putMappingRequest(index).type(type)
            .source(json.toJSONString(), XContentType.JSON);

        client.admin().indices().putMapping(mappingRequest).actionGet();
    }

    /**
     * 新建索引文档
     *
     * @param index  索引名
     * @param type   文档类型
     * @param id     文档ID
     * @param source 文档内容
     * @return 索引结果
     */
    public IndexResponse create(String index, String type, String id, Object source) {
        String sourceStr = JSONObject.toJSONString(source);
        return client.prepareIndex(index, type, id)
            .setSource(sourceStr, XContentType.JSON).execute().actionGet();
    }

    /**
     * 新建索引文档并刷新
     *
     * @param index  索引名
     * @param type   文档类型
     * @param id     文档ID
     * @param source 文档内容
     * @return 索引结果
     */
    public IndexResponse createAndRefresh(String index, String type, String id, Object source) {
        String sourceStr = JSONObject.toJSONString(source);

        return client.prepareIndex(index, type, id)
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
            .setSource(sourceStr, XContentType.JSON).execute().actionGet();
    }

    /**
     * 批量创建索引文档
     *
     * @param index   索引名
     * @param type    文档类型
     * @param sources 文档内容集合
     * @return 索引结果
     */
    public BulkResponse create(String index, String type, Iterable sources) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        sources.forEach(source -> {
            String sourceStr = JSONObject.toJSONString(source);
            bulkRequest.add(client.prepareIndex(index, type)
                .setSource(sourceStr, XContentType.JSON));
        });
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            String message = bulkResponse.buildFailureMessage();
            log.error(message);
        }
        return bulkResponse;
    }


    /**
     * 非阻塞批量创建索引文档
     *
     * @param index   索引名
     * @param type    文档类型
     * @param sources 文档内容集合
     * @return 索引结果
     */
    public void createNoBlock(String index, String type, Iterable sources) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        sources.forEach(source -> {
            String sourceStr = JSONObject.toJSONString(source);
            bulkRequest.add(client.prepareIndex(index, type)
                .setSource(sourceStr, XContentType.JSON));
        });
        bulkRequest.execute();
    }


    /**
     * 更新文档
     *
     * @param index 索引名
     * @param type  文档类型
     * @param id    文档ID
     * @param maps
     */
    public void update(String index, String type, String id, Map<String, Object> maps) {
        String sourceStr = JSONObject.toJSONString(maps);
        client.prepareUpdate(index, type, id)
            .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
            .setDoc(sourceStr, XContentType.JSON)
            .execute().actionGet();
    }

    /**
     * 查询唯一的一条记录
     *
     * @param index 索引名
     * @param type  文档类型
     * @param id
     * @return
     */
    public String get(String index, String type, String id) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery("_id", id));
        SearchResponse searchResponse = client
            .prepareSearch(index)
            .setTypes(type)
            .setPostFilter(boolQueryBuilder)
            .setFetchSource(true)
            .execute()
            .actionGet();

        SearchHits hits = searchResponse.getHits();
        SearchHit hit = hits.getAt(0);
        return hit.getSourceAsString();
    }

    public <T> List<T> findAll(String index,
                               String type,
                               QueryBuilder queryBuilder,
                               Class<T> clazz) {

        SearchRequestBuilder builder = client
            .prepareSearch(index)
            .setTypes(type)
            .setQuery(queryBuilder);

        List<T> list = new ArrayList();

        SearchResponse searchResponse = builder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit searchHit : hits) {
            try {
                T t = JSON.parseObject(searchHit.getSourceAsString(), clazz);
                if (t != null) {
                    list.add(t);
                }
            } catch (Exception ex) {
                throw new ElasticSearchException("Convert json from elasticSearch to object error:", ex);
            }
        }
        return list;
    }

    public List<Map<String, Object>> findAll(String index,
                                             String type,
                                             String[] fields,
                                             QueryBuilder queryBuilder) {

        SearchRequestBuilder builder = client
            .prepareSearch(index)
            .setTypes(type)
            .setFetchSource(fields, null)
            .setQuery(queryBuilder);

        List<Map<String, Object>> list = new ArrayList();

        SearchResponse searchResponse = builder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit searchHit : hits) {
            list.add(searchHit.getSource());
        }
        return list;
    }

    /**
     * 分页查询
     *
     * @param index        索引名
     * @param type         文档类型
     * @param queryBuilder 查询条件
     * @param pageable     分页描述
     * @param clazz        实体类型类
     * @param <T>          类型
     * @return 分页查询结果
     */
    public <T> Page<T> findPage(String index,
                                String type,
                                QueryBuilder queryBuilder,
                                Pageable pageable,
                                Class<T> clazz) {
        SearchRequestBuilder builder = client
            .prepareSearch(index)
            .setTypes(type)
            .setQuery(queryBuilder)
            .setFrom(pageable.getPageNumber() * pageable.getPageSize())
            .setSize(pageable.getPageSize());

        // 处理排序
        if (pageable.getSort() != null) {
            Sort sort = pageable.getSort();
            for (Sort.Order order : sort) {
                String sortField = order.getProperty();
                SortOrder sortOrder = order.getDirection().isDescending() ? SortOrder.DESC : SortOrder.ASC;
                builder.addSort(sortField, sortOrder);
            }
        }

        List<T> list = new ArrayList();
        SearchResponse searchResponse = builder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit searchHit : hits) {
            try {
                T t = JSON.parseObject(searchHit.getSourceAsString(), clazz);
                if (t != null) {
                    list.add(t);
                }
            } catch (Exception ex) {
                throw new ElasticSearchException("Convert json from elasticSearch to object error:", ex);
            }
        }
        return new PageImpl<>(list, pageable, hits.getTotalHits());
    }

    /**
     * 根据条件查询总数
     *
     * @param index        索引名
     * @param type         文档类型
     * @param queryBuilder
     * @return
     */
    public long count(String index, String type, QueryBuilder queryBuilder) {
        return client.prepareSearch(index)
            .setTypes(type)
            .setSize(0)
            .setQuery(queryBuilder)
            .get()
            .getHits()
            .getTotalHits();
    }

    /**
     * 删除整个索引
     *
     * @param index 索引名
     * @param index
     */
    public void removeIndex(String index) {
        try {
            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index)
                .execute().actionGet();
            if (!dResponse.isAcknowledged()) {
                log.error("Fail to delete index : {}", index);
            }
        } catch (IndexNotFoundException e) {
            log.warn("Fail to delete index : " + index);
        }

    }

    public void getTemplate(String name) {
        client.admin().indices().prepareGetTemplates(name).get().getIndexTemplates();
    }

    public void createTemplate(String name, Map<String, Map<String, Object>> mappings) {
        Map<String, Object> settings = new HashMap<>();

        Map<String, Object> mapping = new HashMap<>();
        PutIndexTemplateResponse response = client.admin().indices()
            .preparePutTemplate(name)
            .setSettings(settings)
            .addMapping("", mapping)
            .execute()
            .actionGet();
    }
}
