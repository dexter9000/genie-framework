package com.genie.es.exception;

public class ElasticSearchException extends RuntimeException {

    public ElasticSearchException(String message) {
        super(message);
    }

    public ElasticSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElasticSearchException(Throwable cause) {
        super(cause);
    }
}
