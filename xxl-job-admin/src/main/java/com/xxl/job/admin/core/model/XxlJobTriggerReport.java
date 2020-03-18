package com.xxl.job.admin.core.model;

/**
 * @author: seventeen
 * @Date: 2020/3/17 16:06
 * @description:
 */
public class XxlJobTriggerReport {

    private String day;
    private int triggerDayCount;
    private int triggerDayCountRunning;
    private int triggerDayCountSuc;
    private int triggerDayCountFail;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTriggerDayCount() {
        return triggerDayCount;
    }

    public void setTriggerDayCount(int triggerDayCount) {
        this.triggerDayCount = triggerDayCount;
    }

    public int getTriggerDayCountRunning() {
        return triggerDayCountRunning;
    }

    public void setTriggerDayCountRunning(int triggerDayCountRunning) {
        this.triggerDayCountRunning = triggerDayCountRunning;
    }

    public int getTriggerDayCountSuc() {
        return triggerDayCountSuc;
    }

    public void setTriggerDayCountSuc(int triggerDayCountSuc) {
        this.triggerDayCountSuc = triggerDayCountSuc;
    }

    public int getTriggerDayCountFail() {
        return triggerDayCountFail;
    }

    public void setTriggerDayCountFail(int triggerDayCountFail) {
        this.triggerDayCountFail = triggerDayCountFail;
    }
}
