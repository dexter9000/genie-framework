package com.genie.es.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "genie.elasticsearch", ignoreUnknownFields = false)
public class ElasticSearchProperties {

    private String clusterName = "elasticsearch";   // 集群名称
    private String clusterNodes;                    // 节点数组字符串
    private int batchSize = 10;                     // 批次大小
    private int flushTime = 60000;                  //
    private int threadTimeout = 60000;              // 线程超时时间，用于回收线程

    private Map<String, String> properties = new HashMap();

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getFlushTime() {
        return flushTime;
    }

    public void setFlushTime(int flushTime) {
        this.flushTime = flushTime;
    }

    public int getThreadTimeout() {
        return threadTimeout;
    }

    public void setThreadTimeout(int threadTimeout) {
        this.threadTimeout = threadTimeout;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
