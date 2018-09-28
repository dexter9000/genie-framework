package com.genie.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.UnknownHostException;

import static org.mockito.Mockito.doReturn;

public class ElasticSearchAutoConfigurationTest {

    @Mock
    private ElasticSearchProperties properties;

    @InjectMocks
    private ElasticSearchAutoConfiguration configuration;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        doReturn("localhost:9200").when(properties).getClusterNodes();
        doReturn("elasticsearch").when(properties).getClusterName();
    }

    @Test
    public void elasticSearchOperations() throws UnknownHostException {
        TransportClient client = configuration.client();
        configuration.elasticSearchOperations(client);
    }
}
