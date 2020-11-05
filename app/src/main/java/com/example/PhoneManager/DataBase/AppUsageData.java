/**
 * app使用信息数据表
 */

package com.example.PhoneManager.DataBase;

import org.litepal.crud.DataSupport;

public class AppUsageData extends DataSupport {
    private String appName;
    private long timeInForeground;
    private int launchCount;
    private String FirstRunningTime,LastRunningTime;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getTimeInForeground() {
        return timeInForeground;
    }

    public void setTimeInForeground(long timeInForeground) {
        this.timeInForeground = timeInForeground;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    public String getFirstRunningTime() {
        return FirstRunningTime;
    }

    public void setFirstRunningTime(String firstRunningTime) {
        FirstRunningTime = firstRunningTime;
    }

    public String  getLastRunningTime() {
        return LastRunningTime;
    }

    public void setLastRunningTime(String lastRunningTime) {
        LastRunningTime = lastRunningTime;
    }
}
