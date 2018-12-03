package com.genie.flow;

import com.genie.flow.task.BaseTask;
import com.genie.flow.task.ServiceTask;
import com.genie.flow.task.StartTask;
import org.flowable.bpmn.model.FlowNode;

import java.util.Map;

public class TaskFactory {

    private Map<Class, String> task;

    public void initFactory(){

    }

    public static BaseTask createTask(FlowNode flowNode){
        BaseTask task = null;
        switch (flowNode.getClass().getSimpleName()){
            case "StartEvent":
                task = new StartTask(flowNode);
                break;
            case "ServiceTask":
                task = new ServiceTask(flowNode);
                break;
        }
        return task;
    }
}
