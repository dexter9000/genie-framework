package com.genie.flow.domain;

import com.genie.flow.enumeration.FactStatus;
import lombok.ToString;

import java.io.Serializable;
import java.time.ZonedDateTime;

@ToString
public class Fact implements Serializable {

    /**
     * id = campaignId + '_' + metaId
     * FIXME 如果一个会员可以在一个流程中同时出现在多个位置，则算法变为 id = taskId + '_' + metaId
     */
//    @Id
    private String id;
    /**
     * 流程进程ID
     */
    private String processId;
    /**
     * 当前TaskId
     */
    private String taskId;
    private String batchCode;
    /**
     * 元数据ID，用于识别流数据
     */
    private String metaId;
    /**
     * 扩展字段，JSON形式保存
     */
    private String metadata;
    /**
     * 时间戳
     */
    private transient ZonedDateTime timestamp;
    /**
     * 状态
     */
    private FactStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetaId() {
        return metaId;
    }

    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public FactStatus getStatus() {
        return status;
    }

    public void setStatus(FactStatus status) {
        this.status = status;
    }
}
