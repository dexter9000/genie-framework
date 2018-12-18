package com.genie.flow.service;

import com.genie.flow.enumeration.TaskType;
import com.genie.flow.model.FlowInstance;
import com.genie.flow.model.FlowModel;
import com.genie.flow.process.StartTaskProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowService {

    private static final Logger log = LoggerFactory.getLogger(FlowService.class);

    private FlowRepositoryService flowRepositoryService;
    private TaskService taskService;

    /**
     * 启动新的流程，只有一个服务实例会执行启动动作
     * 然后同步任务给其他服务实例
     */
    public void startFlow(String campaignId) {
        log.info("Start workflow : {}", campaignId);
        long startTime = System.currentTimeMillis();
        // 为防止重复触发，先插入空对象
        FlowInstance instance = new FlowInstance();
        instance.setId(campaignId);
        addFlowInstance(instance);
        FlowModel flowModel = getFlowModel(campaignId);
        instance = flowRepositoryService.loadWorkflow(flowModel);

        if (null == instance) {
            log.error("FLow [{}] not found", campaignId);
            return;
        }

        // Task初始化
        instance.getTasks().forEach(taskProcess -> {
            taskProcess.init();
        });

        // 启动StartTask, 不需要独立线程
        StartTaskProcess startTaskProcess = (StartTaskProcess) instance.getTasks().stream()
            .filter(task -> task.getType().equals(TaskType.START))
            .findFirst().orElse(null);
        if (startTaskProcess == null) {
            log.error("Flow [{}] need Start task.", instance.getId());
            return;
        }
        taskService.startTask(startTaskProcess.getTask().getId());
        long endTime = System.currentTimeMillis();
        log.info("Start workflow [{}] in {} ms.", campaignId, (endTime - startTime));
    }

    public void addFlowInstance(FlowInstance workflow){
        GlobalCache.addCache("FlowInstance", workflow.getId(), workflow);
    }

    private FlowModel getFlowModel(String id){
        // TODO
        return null;
    }
}
