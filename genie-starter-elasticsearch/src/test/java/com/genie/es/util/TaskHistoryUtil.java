package com.genie.es.util;

import com.genie.es.entity.TaskHistory;

import java.util.UUID;

public class TaskHistoryUtil {

    public static TaskHistory createTaskHistory(String campaignId) {
        TaskHistory history = new TaskHistory();
        history.setCampaignId(campaignId);
        history.setTaskId(1);
        history.setFactId(UUID.randomUUID().toString());
        return history;
    }

}
