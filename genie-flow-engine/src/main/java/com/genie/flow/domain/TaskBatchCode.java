package com.genie.flow.domain;

import lombok.ToString;

@ToString
public class TaskBatchCode {

    /**
     * 取Task Id
     */
    private String id;

    private String batchCode;

    public TaskBatchCode() {
    }

    public TaskBatchCode(String id, String batchCode) {
        this.id = id;
        this.batchCode = batchCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }
}
