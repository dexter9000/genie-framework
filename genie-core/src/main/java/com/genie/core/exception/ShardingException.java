package com.genie.core.exception;

public class ShardingException extends Exception {

    public ShardingException(String message) {
        super(message);
    }

    public ShardingException(String message, Throwable cause) {
        super(message, cause);
    }
}
