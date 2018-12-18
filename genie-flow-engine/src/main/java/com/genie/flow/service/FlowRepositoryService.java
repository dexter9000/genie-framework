package com.genie.flow.service;

import com.alibaba.fastjson.JSONObject;
import com.genie.core.exception.ServiceException;
import com.genie.flow.model.FlowInstance;
import com.genie.flow.model.FlowModel;
import com.genie.flow.model.ProcessInstance;
import com.genie.flow.model.TaskNode;
import com.genie.flow.process.BaseTaskProcess;
import com.genie.flow.task.BaseTask;
import com.genie.flow.task.StreamTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程仓库服务
 */
public class FlowRepositoryService {
    private static final Logger log = LoggerFactory.getLogger(FlowRepositoryService.class);

    private TaskService taskService;


    public void createFlowModel() {
        // TODO 判重
    }

    public void getFlowModel(String modelId) {
        // TODO 需要缓存
    }

    public void deleteFlowModel(String modelId) {
        // TODO 判断流程状态
    }

    public void createFlowInstance(String modelId) {
        // TODO 判断流程模板状态
    }

    public ProcessInstance getFlowInstance(String instanceId) {
        // TODO 需要缓存
        return null;
    }

    public void deleteFlowInstance(String instanceId) {
        // TODO

    }

    public BaseTask getTask(String taskId) {
        // TODO
        return null;
    }

    public StreamTask getStreamTask(String instanceId, String taskId) {

        ProcessInstance processInstance = null;

//        processInstance
        return null;
    }

    public void initWorkflow() {
        log.debug("Init Workflow");
//        WorkflowCache.values().forEach(workflow -> cleanWorkflow(workflow.getId()));
//        loadAllWorkflow();
    }


    /**
     * 系统启动时加载当前所有启动的workflow
     */
    public void loadAllWorkflow() {
        long startTime = System.currentTimeMillis();
        // TODO 加载所有活动流程
//        campaignRepository.findAll()
//            .stream()
//            .filter(campaign ->
//                campaign.getCampaignType().equals(CampaignType.CRM)
//                    && campaign.getStatus().equals(CampaignStatus.RUNNING))
//            .forEach(campaign -> loadWorkflow(campaign.getId()));
        long endTime = System.currentTimeMillis();

        log.info("Load all workflows in {} ms.", (endTime - startTime));
    }


    /**
     * 加载指定的workflow
     *
     * @param flowModel
     */
    public FlowInstance loadWorkflow(FlowModel flowModel) {
        log.info("Load workflow : {}", flowModel);


        log.info("Build Task Process for Campaign : {}", flowModel.getId());
        List<BaseTaskProcess> taskProcessList = new ArrayList<>();
        // 解析task
        String tasks = flowModel.getTasks();
        List<TaskNode> nodeList = JSONObject.parseArray(tasks, TaskNode.class);
        nodeList.forEach(node -> {
            try {
                BaseTaskProcess process = taskService.buildTaskProcess(node);
                taskProcessList.add(process);
                addTaskProcess(process);
                addTask(process.getTask());
            } catch (ServiceException e) {
                log.error("Load Workflow, Task [{}] is null!", node.getTaskId());
            }
        });

        // 解析workflow
        FlowInstance workflow = new FlowInstance();

//        workflow.setStartTime(campaign.getStartTime());
        workflow.setModelId(flowModel.getId());
        workflow.setTasks(taskProcessList);

        addFlowInstance(workflow);

        return workflow;
    }

    public void addTask(BaseTask task){
        GlobalCache.addCache("Task", task.getId(), task);
    }

    public void addTaskProcess(BaseTaskProcess process){
        GlobalCache.addCache("TaskProcess", process.getId(), process);
    }

    public BaseTaskProcess getTaskProcess(String processId){
        return (BaseTaskProcess) GlobalCache.getCache("TaskProcess", processId);
    }

    public void addFlowInstance(FlowInstance workflow){
        GlobalCache.addCache("FlowInstance", workflow.getId(), workflow);
    }
}
