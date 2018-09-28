package com.genie.core.web.rest.errors;

import java.io.Serializable;

/**
 * 错误字段描述，用于后台校验失败后向前台传递消息
 */
public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;
    private final String field;
    private final String message;
    private final String defaultMessage;
    private final transient Object[] arguments;

    public FieldErrorVM(String dto, String field, String message, Object[] arguments) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
        this.arguments = arguments;
        this.defaultMessage = "";
    }

    public FieldErrorVM(String dto, String field, String message, Object[] arguments, String defaultMessage) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
