package com.example.PhoneManager.BottomFragments.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class UsingRecord {
    private String AppName;
    private String Time;
    private Bitmap AppIcon;

    public UsingRecord(String appName) {
        AppName = appName;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Bitmap getAppIcon() {
        return AppIcon;
    }

    public void setAppIcon(Bitmap appIcon) {
        AppIcon = appIcon;
    }
}
