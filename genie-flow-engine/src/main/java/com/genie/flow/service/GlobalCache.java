package com.genie.flow.service;

import com.genie.flow.task.BaseTask;

import java.util.HashMap;
import java.util.Map;

public class GlobalCache {

    private static Map<String, Map<String, Object>> caches = new HashMap<>();

    private static Map<String, BaseTask> tasks = new HashMap<>();


    public static Object addCache(String cacheName, String key, Object val){
        Map<String, Object> cache = caches.get(cacheName);
        if(cache == null){
            cache = new HashMap<>();
            caches.put(cacheName, cache);
        }
        return cache.put(key, val);
    }

    public static Object getCache(String cacheName, String key){
        Map<String, Object> cache = caches.get(cacheName);
        if(cache == null){
            return null;
        }
        return cache.get(key);
    }

    public static BaseTask addTask(BaseTask task){

        return tasks.put(task.getId(), task);
    }

    public static BaseTask getTask(String taskId){

        return tasks.get(taskId);
    }


}
