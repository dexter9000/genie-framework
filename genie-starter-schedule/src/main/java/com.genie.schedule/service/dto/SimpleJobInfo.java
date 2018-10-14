package com.genie.schedule.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @ClassName: JobInfo
 */
public class SimpleJobInfo extends JobInfo {

    //执行频率: ?分钟执行一次
    @NotNull
    @Pattern(regexp = "[1-9]+", message = "rate is wrong number!")
    private String rate;

    //执行次数: ?次  (-1表示永远执行下去)
    @NotNull
    @Pattern(regexp = "-1|\\d+", message = "times is wrong number!")
    private String times;


    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

}
