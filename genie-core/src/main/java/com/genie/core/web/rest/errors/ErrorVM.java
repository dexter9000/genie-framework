package com.genie.core.web.rest.errors;

import com.genie.core.utils.RandomUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * View Model for transferring error message with a list of field errors.
 */
public class ErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private String errorId;
    private List<String> message = new ArrayList<>();           // 错误消息
    private List<String> description;       // 错误描述
    private String cause;                   // 错误原因，未知异常需要记录出错原因
    private Map<String, String> params;
    private String entityName;              // 错误实体
    private List<FieldErrorVM> fieldErrors; // 错误字段

    public ErrorVM(String message) {
        this(message, "");
    }

    public ErrorVM(String message, String description) {
        this(message, description, "", null);
    }

    public ErrorVM(String message, String description, String entityName, List<FieldErrorVM> fieldErrors) {
        this(Arrays.asList(message), Arrays.asList(description), "", null);
    }

    public ErrorVM(List<String> message, List<String> description, String entityName, List<FieldErrorVM> fieldErrors) {
        this.errorId = RandomUtil.generateErrorId();
        this.message = message;
        this.entityName = entityName;
        this.description = description;
        this.fieldErrors = fieldErrors;
    }

    public void add(FieldErrorVM fieldError) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(fieldError);
    }

    public String getErrorId() {
        return errorId;
    }

    public List<String> getMessage() {
        return message;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<FieldErrorVM> getFieldErrors() {
        return fieldErrors;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
