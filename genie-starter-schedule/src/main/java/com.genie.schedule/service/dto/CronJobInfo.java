package com.genie.schedule.service.dto;

import javax.validation.constraints.NotNull;

public class CronJobInfo extends JobInfo {

    @NotNull(message = "Cron Expression is null!")
    private String cronExpression;

    private String secondField;

    private String minutesField;

    private String hourField;

    private String dayField;

    private String monthField;

    private String weekField;

    private String timeZone;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }


    public String getSecondField() {
        return secondField;
    }

    public void setSecondField(String secondField) {
        this.secondField = secondField;
    }

    public String getMinutesField() {
        return minutesField;
    }

    public void setMinutesField(String minutesField) {
        this.minutesField = minutesField;
    }

    public String getHourField() {
        return hourField;
    }

    public void setHourField(String hourField) {
        this.hourField = hourField;
    }

    public String getDayField() {
        return dayField;
    }

    public void setDayField(String dayField) {
        this.dayField = dayField;
    }

    public String getMonthField() {
        return monthField;
    }

    public void setMonthField(String monthField) {
        this.monthField = monthField;
    }

    public String getWeekField() {
        return weekField;
    }

    public void setWeekField(String weekField) {
        this.weekField = weekField;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
