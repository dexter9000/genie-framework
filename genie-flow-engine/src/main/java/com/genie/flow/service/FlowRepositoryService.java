package com.genie.flow.service;

import com.genie.flow.model.ProcessInstance;
import com.genie.flow.task.BaseTask;
import com.genie.flow.task.StreamTask;

/**
 * 流程仓库服务
 */
public class FlowRepositoryService {

    public void createFlowModel(){
        // TODO 判重
    }

    public void getFlowModel(String modelId){
        // TODO 需要缓存
    }

    public void deleteFlowModel(String modelId){
        // TODO 判断流程状态
    }

    public void createFlowInstance(String modelId){
        // TODO 判断流程模板状态
    }

    public ProcessInstance getFlowInstance(String instanceId){
        // TODO 需要缓存
        return null;
    }

    public void deleteFlowInstance(String instanceId){

    }

    public BaseTask getTask(String taskId){
        return null;
    }


    public StreamTask getStreamTask(String instanceId, String taskId){

        ProcessInstance processInstance = null;

//        processInstance
        return null;
    }
}
