package com.genie.schedule.service.dto;

import java.io.Serializable;

/**
 * Created by meng013 on 2017/11/7.
 */
public class SimpleTriggerDTO extends AbstractTriggerDTO implements Serializable {

    private int repeatCount = 0;
    private long repeatInterval = 0;
    private int timesTriggered = 0;
    private boolean complete = false;

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public int getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(int timesTriggered) {
        this.timesTriggered = timesTriggered;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
