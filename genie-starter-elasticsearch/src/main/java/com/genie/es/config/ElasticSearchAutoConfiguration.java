package com.genie.es.config;

import com.genie.es.repository.ElasticSearchOperations;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Configuration
@ConditionalOnClass(TransportClient.class)
@EnableConfigurationProperties({ElasticSearchProperties.class})
public class ElasticSearchAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(ElasticSearchAutoConfiguration.class);

    @Autowired
    private ElasticSearchProperties properties;

    @Bean
    public TransportClient client() throws UnknownHostException {
        log.debug("Create TransportClient");
        TransportClient client = new PreBuiltTransportClient(settings()).addTransportAddress(transportAddress()); // NOSONAR
        return client;
    }

    @Bean
    public ElasticSearchOperations elasticSearchOperations(TransportClient client) {
        return new ElasticSearchOperations(client);
    }

    private Settings settings() {
        return Settings.builder().put("cluster.name", properties.getClusterName()) //设置ES实例的名称my-application
                //.put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .build();
    }

    private TransportAddress transportAddress() throws UnknownHostException {
        String[] host = properties.getClusterNodes().split(":");
        Integer port = Integer.parseInt(host[1]);
        InetAddress addr = InetAddress.getByName(host[0]);
        InetSocketAddress ip = new InetSocketAddress(addr, port);
        TransportAddress transportAddress = new InetSocketTransportAddress(ip);
        return transportAddress;
    }

}
