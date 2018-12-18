package com.genie.flow;

import com.genie.flow.domain.Fact;
import com.genie.flow.domain.TaskHistory;
import com.genie.flow.enumeration.TaskStatus;
import com.genie.flow.model.FactResultDTO;
import com.genie.flow.model.ProcessInstance;
import com.genie.flow.service.FlowRepositoryService;
import com.genie.flow.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FlowEngine {

    private final Logger log = LoggerFactory.getLogger(FlowEngine.class);

    @Autowired
    private FlowRepositoryService flowRepositoryService;
    /**
     * 从MQ接收到数据后的处理逻辑
     *
     * @param factResult
     */
    public void receiveFact(FactResultDTO factResult) {
        ProcessInstance workflow = flowRepositoryService.getFlowInstance(factResult.getProcessId());
        // 判断活动状态
        if (workflow == null) {
            log.error("FlowEngine_receiveFact_workflow_is_null {}", factResult.getProcessId());
            return;
        }

        // FIXME : 暂停fact读写
        Fact fact = toFact(factResult);

        // TODO 如果对应数据的任务已经执行过了，根据重复标记决定是否重复执行，默认不重复执行

        BaseTask taskProcess = flowRepositoryService.getTask(fact.getTaskId());
        if (taskProcess == null) {
            log.error("FlowEngine_dealTask_taskProcess_is_null");
            // 任务不存在，记入缓存池
            return;
        }

        //TaskHistory history = createTaskHistory(fact);
        //taskProcess.saveTaskHistory(history);

        fact.setTaskId(taskProcess.nextTask(factResult));
        dealTask(taskProcess, fact);
    }

    private Fact toFact(FactResultDTO factResult){
        // TODO
        return null;
    }

    private TaskHistory createTaskHistory(Fact fact) {
        TaskHistory history = new TaskHistory();
        history.setFactId(fact.getId());
        history.setTaskId(fact.getTaskId());
        history.setCampaignId(fact.getProcessId());
        history.setMetaId(fact.getMetaId());
        ZonedDateTime endTime = ZonedDateTime.now(ZoneId.of("UTC"));
        history.setEndTime(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z");
        return history;
    }

    /**
     * 处理流程数据
     *
     * @param fact 流程数据
     * @return 结果数据
     */
    private Fact dealTask(BaseTask taskProcess, Fact fact) {
        // 查找当前任务
        if (!taskProcess.getStatus().equals(TaskStatus.READY)
            && !taskProcess.getStatus().equals(TaskStatus.RUNNING)) {
            log.error("FlowEngine_dealTask_taskProcess_status {}", taskProcess.getStatus());
            // 任务状态不能正常执行，直接丢弃数据
            return null;
        }

        fact = taskProcess.execute(fact);
        // 更新fact状态
//        factService.update(fact);
        return fact;
    }
}
