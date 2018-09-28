package com.genie.schedule.utils;

import org.quartz.*;

public class ListenerUnits implements SchedulerListener {

    @Override
    public void jobScheduled(Trigger trigger) {
        System.out.println("jobScheduled");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        System.out.println("jobUnscheduled");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        System.out.println("triggerFinalized");
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        System.out.println("triggerPaused");
    }

    @Override
    public void triggersPaused(String s) {
        System.out.println("triggersPaused");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        System.out.println("triggerResumed");
    }

    @Override
    public void triggersResumed(String s) {
        System.out.println("triggersResumed");
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        System.out.println("jobAdded");
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        System.out.println("jobDeleted");
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        System.out.println("jobPaused");
    }

    @Override
    public void jobsPaused(String s) {
        System.out.println("jobsPaused");
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        System.out.println("jobResumed");
    }

    @Override
    public void jobsResumed(String s) {
        System.out.println("jobsResumed");
    }

    @Override
    public void schedulerError(String s, SchedulerException e) {
        System.out.println("schedulerError");
    }

    @Override
    public void schedulerInStandbyMode() {
        System.out.println("schedulerInStandbyMode");
    }

    @Override
    public void schedulerStarted() {
        System.out.println("schedulerStarted");
    }

    @Override
    public void schedulerStarting() {
        System.out.println("schedulerStarting");
    }

    @Override
    public void schedulerShutdown() {
        System.out.println("schedulerShutdown");
    }

    @Override
    public void schedulerShuttingdown() {
        System.out.println("schedulerShuttingdown");
    }

    @Override
    public void schedulingDataCleared() {
        System.out.println("schedulingDataCleared");
    }
}
