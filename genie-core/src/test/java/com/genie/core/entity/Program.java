package com.genie.core.entity;


import com.genie.core.annotation.ZonedDateTimeFormat;

import java.time.ZonedDateTime;

public class Program {

    /**
     * 名称
     */
    private String name;
    /**
     * 查询开始时间
     */
    @ZonedDateTimeFormat
    private ZonedDateTime startTime;
    /**
     * 查询结束时间
     */
    @ZonedDateTimeFormat
    private ZonedDateTime endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Program{" +
            "name='" + name + '\'' +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
    }
}
