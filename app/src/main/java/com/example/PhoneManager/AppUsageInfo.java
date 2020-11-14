package com.example.PhoneManager;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

public  class AppUsageInfo {
        Bitmap appIcon; // You may add get this usage data also, if you wish.
        String appName, packageName;
        Integer timeInForeground;
        float timeInForegroundPercentage;
        Integer LastRuntime;
        int launchCount;
        long FirstRunningTime,LastRunningTime,LastBackTime;
        long[] EachHourRunningTimes;
        int[] EachHourLaunchCounts;
    //以包名为形参的构造函数
    AppUsageInfo(String pName) {
        this.packageName=pName;
    }

    public Bitmap getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Bitmap appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getLastRuntime() {
        return LastRuntime;
    }

    public void setLastRuntime(Integer lastRuntime) {
        LastRuntime = lastRuntime;
    }

    public Integer getTimeInForeground() {
        return timeInForeground;
    }

    public void setTimeInForeground(Integer timeInForeground) {
        this.timeInForeground = timeInForeground;
    }

    public float getTimeInForegroundPercentage() {
        return timeInForegroundPercentage;
    }

    public void setTimeInForegroundPercentage(float timeInForegroundPercentage) {
        this.timeInForegroundPercentage = timeInForegroundPercentage;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    public long getFirstRunningTime() {
        return FirstRunningTime;
    }

    public void setFirstRunningTime(long firstRunningTime) {
        FirstRunningTime = firstRunningTime;
    }

    public long getLastRunningTime() {
        return LastRunningTime;
    }

    public void setLastRunningTime(long lastRunningTime) {
        LastRunningTime = lastRunningTime;
    }

    public long getLastBackTime() {
        return LastBackTime;
    }

    public void setLastBackTime(long lastBackTime) {
        LastBackTime = lastBackTime;
    }

    public long[] getEachHourRunningTimes() {
        return EachHourRunningTimes;
    }

    public void setEachHourRunningTimes(long[] eachHourRunningTimes) {
        EachHourRunningTimes = eachHourRunningTimes;
    }

    public int[] getEachHourLaunchCounts() {
        return EachHourLaunchCounts;
    }

    public void setEachHourLaunchCounts(int[] eachHourLaunchCounts) {
        EachHourLaunchCounts = eachHourLaunchCounts;
    }
}
