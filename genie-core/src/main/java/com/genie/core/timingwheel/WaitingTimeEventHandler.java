package com.genie.core.timingwheel;

import com.genie.core.batch.BatchHandler;

import java.util.List;

public class WaitingTimeEventHandler extends BatchHandler<String> {

    public WaitingTimeEventHandler(int batchSize, int flushTime, int threadTimeout) {
        super(batchSize, flushTime, threadTimeout);
    }

    @Override
    public void doProcess(String key, List<String> list) {
        WaitingTimeEvent waitingTimeEvent = new WaitingTimeEvent();
        String[] dataArr = key.split(",");
        waitingTimeEvent.setWheelId(dataArr[0]);
        waitingTimeEvent.setExpireTime(dataArr[1]);
        waitingTimeEvent.setDataList(list);
//        waitingAudienceTimeEventRepository.save(waitingTimeEvent);
    }

}
