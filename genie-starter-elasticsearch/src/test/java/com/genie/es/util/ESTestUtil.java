package com.genie.es.util;

import com.genie.es.config.ElasticSearchProperties;
import com.genie.es.repository.ElasticSearchOperations;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * 测试工具类
 */
public class ESTestUtil {

    public static ElasticSearchProperties createElasticSearchProperties(){
        ElasticSearchProperties elasticSearchProperties = new ElasticSearchProperties();
        elasticSearchProperties.setBatchSize(50);
        elasticSearchProperties.setFlushTime(1000);
        return elasticSearchProperties;
    }

    public static ElasticSearchProperties createElasticSearchProperties(String clusterName, String clusterNodes) {
        ElasticSearchProperties elasticSearchProperties = new ElasticSearchProperties();
        elasticSearchProperties.setClusterName(clusterName);
        elasticSearchProperties.setClusterNodes(clusterNodes);
        elasticSearchProperties.setBatchSize(10);
        elasticSearchProperties.setFlushTime(1000);
        return elasticSearchProperties;
    }

    public static ElasticSearchOperations createElasticSearchOperations(Client client) throws UnknownHostException {
        return new ElasticSearchOperations(client);
    }

    public static ElasticsearchTemplate createElasticsearchTemplate(Client client) throws UnknownHostException {
        return new ElasticsearchTemplate(client);
    }

    public static ElasticSearchOperations createElasticSearchOperations(ElasticSearchProperties elasticSearchProperties) throws UnknownHostException {
        TransportClient client;
        Settings settings = Settings.builder()
                .put("cluster.name", elasticSearchProperties.getClusterName())
                .build();

        TransportAddress transportAddress = transportAddress(elasticSearchProperties);

        client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);

        return new ElasticSearchOperations(client);
    }

    private static TransportAddress transportAddress(ElasticSearchProperties elasticSearchProperties) throws UnknownHostException {
        String[] host = elasticSearchProperties.getClusterNodes().split(":");
        Integer port = Integer.parseInt(host[1]);
        InetAddress addr = InetAddress.getByName(host[0]);
        InetSocketAddress ip = new InetSocketAddress(addr, port);
        TransportAddress transportAddress = new InetSocketTransportAddress(ip);
        return transportAddress;
    }

}
