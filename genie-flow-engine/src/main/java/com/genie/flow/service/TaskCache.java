package com.genie.flow.service;

import com.genie.flow.task.BaseTask;

import java.util.HashMap;
import java.util.Map;

public class TaskCache {

    private static Map<String, BaseTask> tasks = new HashMap<>();

    public static BaseTask addTask(BaseTask task){

        return tasks.put(task.getId(), task);
    }

    public static BaseTask getTask(String taskId){

        return tasks.get(taskId);
    }


}
