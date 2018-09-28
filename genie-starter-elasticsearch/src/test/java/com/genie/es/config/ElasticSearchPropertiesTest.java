package com.genie.es.config;

import org.junit.Test;

public class ElasticSearchPropertiesTest {

    @Test
    public void getAndSet(){
        ElasticSearchProperties properties = new ElasticSearchProperties();

        properties.setClusterName("name");
        properties.setClusterNodes("nodes");
        properties.setFlushTime(100);
        properties.setBatchSize(100);
        properties.setProperties(null);
        properties.setThreadTimeout(100);

        properties.getClusterName();
        properties.getClusterNodes();
        properties.getBatchSize();
        properties.getFlushTime();
        properties.getThreadTimeout();
        properties.getProperties();
    }
}
