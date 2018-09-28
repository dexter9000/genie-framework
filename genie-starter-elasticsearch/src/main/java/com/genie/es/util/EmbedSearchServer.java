package com.genie.es.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.transport.Netty4Plugin;

import java.io.IOException;
import java.util.Arrays;

public class EmbedSearchServer {

    public static final String ES_DATA_PATH = "target/elasticsearch";

    private Node node;

    public EmbedSearchServer() {
        this(ES_DATA_PATH);
    }

    public EmbedSearchServer(String dataPath) {
        node = new EmbedNode(
                Settings.builder()
                        .put("transport.type", "netty4")
                        .put("http.type", "netty4")
                        .put("http.enabled", "true")
                        .put("path.home", "classpath")
                        .put("path.data", dataPath)
                        .build(),
                Arrays.asList(Netty4Plugin.class)
        );
    }

    public void start() throws NodeValidationException {
        node.start();
    }

    public void stop() throws IOException {
        node.close();
    }

    public Client getClient() {
        return node.client();
    }
}
