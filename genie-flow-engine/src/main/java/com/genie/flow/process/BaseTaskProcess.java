package com.genie.flow.process;

import com.genie.core.exception.ServiceException;
import com.genie.flow.domain.Fact;
import com.genie.flow.domain.TaskHistory;
import com.genie.flow.enumeration.TaskStatus;
import com.genie.flow.enumeration.TaskType;
import com.genie.flow.model.FactResultDTO;
import com.genie.flow.model.TaskNode;
import com.genie.flow.service.TaskSupport;
import com.genie.flow.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

public abstract class BaseTaskProcess {

    private static final Logger log = LoggerFactory.getLogger(BaseTaskProcess.class);

    protected String id;
    /**
     * 活动ID
     */
    protected String campaignId;
    protected String campaignName;
    protected String batchCode;
    /**
     * 任务类型
     */
    protected TaskType type;
    protected TaskStatus status;
    protected boolean scheduled = false;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;
    protected TaskNode taskNode;

    /**
     * 下一个任务，普通任务时的下一个任务，规则任务的默认下一个任务
     */
    protected String nextTaskId;

    public BaseTaskProcess(BaseTask task, TaskNode taskNode) {
        this.id = task.getId();
        this.campaignId = task.getFlowId();
        this.status = task.getStatus();
        this.taskNode = taskNode;
    }

    public BaseTaskProcess(BaseTask task, TaskSupport support) {
        this.id = task.getId();
        this.campaignId = task.getFlowId();
        this.status = task.getStatus();
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String nextTask(FactResultDTO factResult) {
        log.debug("next task is : {}", nextTaskId);
        return nextTaskId;
    }

    public void init() {
        log.debug("init");
    }

    public void close() {
        log.debug("close");
    }

    /**
     *
     * @param fact
     * @return
     */
    public Fact execute(Fact fact) {
        beforeWork();
        fact = doProcess(fact);
        fact = afterWork(fact);
        return fact;
    }

    /**
     * 监听队列，处理单条Fact数据，用于具体业务处理器覆盖该方法
     *
     * @param fact
     * @return
     */
    public Fact doProcess(Fact fact) {
        if (this.status.equals(TaskStatus.PENDING)) {
            log.error("BaseTaskProcess_doProcess_task_is_pending {}", this.id);
        }
        throw new ServiceException("");
    }

    /**
     * 基于触发条件判断任务是否执行
     */
    public void beforeWork() {
        log.debug("beforeWork");
        // TODO 记录任务状态
        // TODO 任务条件
    }

    /**
     * 任务后执行
     */
    public Fact afterWork(Fact fact) {
        log.debug("afterWork");
        // 任务结果统计
        return fact;
    }

    /**
     * 记录任务执行历史
     */
    public void saveTaskHistory(TaskHistory history) {
        log.debug("saveTaskHistory");
        // TODO: 写历史记录
//        taskHistoryRepository.save(history);
    }

    public void startBatchTask(String batchCode) {

    }

    public abstract String getNextTaskId();

    public abstract BaseTask getTask();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
