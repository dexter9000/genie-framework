package com.genie.es.util;

import com.genie.es.entity.TaskHistory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntityUtilTest {

    @Test
    public void getIndex() {
        TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_001");
        String result = EntityUtil.getIndex(taskHistory.getClass());
        assertEquals("task_history_", result);
    }

    @Test
    public void getShardingId() {
        TaskHistory taskHistory = TaskHistoryUtil.createTaskHistory("camp_001");
        String result = EntityUtil.getShardingId(taskHistory);
        assertEquals("camp_001", result);
    }
}
