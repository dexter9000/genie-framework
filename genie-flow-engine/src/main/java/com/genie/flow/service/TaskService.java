package com.genie.flow.service;

import com.genie.core.exception.ServiceException;
import com.genie.flow.enumeration.TaskStatus;
import com.genie.flow.model.TaskNode;
import com.genie.flow.process.BaseTaskProcess;
import com.genie.flow.process.StartTaskProcess;
import com.genie.flow.task.BaseTask;
import com.genie.flow.task.StartTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    /**
     * 将task转换成TaskProcess
     *
     * @param node 每个task对象
     * @return
     */
    public BaseTaskProcess buildTaskProcess(TaskNode node) {
        BaseTaskProcess baseTaskProcess = null;
        BaseTask task = findTask(node.getTaskId());
        if (task == null) {
            log.error("TaskService_buildTaskProcess_task_is_null {}", node.getTaskId());
            throw new ServiceException("error.service.flow.taskNotFound");
        }
        String batchCode = findBatchCode(task.getId());
        switch (node.getType()) {
            case START:
                baseTaskProcess = new StartTaskProcess((StartTask) task, node);
                break;
//            case END:
//                baseTaskProcess = new EndTaskProcess((EndTask) task);
//                break;
//            case AUDIENCE:
//                baseTaskProcess = new AudienceTaskProcess((AudienceTask) task, taskSupport);
//                break;
//            case CONDITION:
//                baseTaskProcess = new ConditionTaskProcess((ConditionTask) task, campaignFlowTask.getElseToTaskId(), taskSupport);
//                break;
            default:
                log.error("TaskService_buildTaskProcess_task_is_not_support {}", node.getType());
                throw new ServiceException("error.service.flow.taskNotSupport", "Task type is not support");
        }

//        if (campaignFlowTask.getType() != TaskType.END) {
//            baseTaskProcess.setNextTaskId(campaignFlowTask.getToTaskId());
//        }

        return baseTaskProcess;
    }

    private BaseTask findTask(String taskId){
        // TODO
        return null;
    }

    private String findBatchCode(String taskId){
        // TODO
        return null;
    }

    private BaseTaskProcess getTaskProcess(String taskId){
        return (BaseTaskProcess) GlobalCache.getCache("TaskProcess", taskId);
    }

    /**
     * 启动任务，针对START,END和批量任务
     *
     * @param taskId
     */
    public void startTask(String taskId) {
        BaseTaskProcess taskProcess = getTaskProcess(taskId);

        switch (taskProcess.getType()) {
            case START:
                // 直接执行下一个任务
                startTask(taskProcess.getNextTaskId());
                break;
            case END:
            case AUDIENCE:
            case CONDITION:
                startTask(taskProcess);
                break;
            case COMMUNICATION:
            case COUPON:
                // COMMUNICATION和COUPON通过方法调用的都是批量Task
//                startBatchTask(taskProcess);
                break;
            default:
                throw new ServiceException("error.workflow.taskTypeWrong");
        }
    }


    /**
     * 启动任务
     *
     * @param taskProcess
     */
    public void startTask(BaseTaskProcess taskProcess) {
        // 更新任务状态
        taskProcess.setStatus(TaskStatus.RUNNING);
        //taskProcess.startTask();
    }

}
