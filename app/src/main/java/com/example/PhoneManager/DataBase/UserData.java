package com.example.PhoneManager.DataBase;

import org.litepal.crud.DataSupport;

public class UserData extends DataSupport {
    private String AppName; //app名称
    private String FirstRunningTime; //第一次启动时间
    private String LastRunningTime;  //最后一次启动时间
    private long TotalRunningTime; //总运行时间
    private int TotalLaunchCount;  //总运行次数

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getFirstRunningTime() {
        return FirstRunningTime;
    }

    public void setFirstRunningTime(String firstRunningTime) {
        FirstRunningTime = firstRunningTime;
    }

    public String getLastRunningTime() {
        return LastRunningTime;
    }

    public void setLastRunningTime(String lastRunningTime) {
        LastRunningTime = lastRunningTime;
    }

    public long getTotalRunningTime() {
        return TotalRunningTime;
    }

    public void setTotalRunningTime(long totalRunningTime) {
        TotalRunningTime = totalRunningTime;
    }

    public int getTotalLaunchCount() {
        return TotalLaunchCount;
    }

    public void setTotalLaunchCount(int totalLaunchCount) {
        TotalLaunchCount = totalLaunchCount;
    }
}
