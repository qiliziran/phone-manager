package com.example.PhoneManager.BottomFragments.home.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class SearchItemBean {
    private Bitmap AppIcon;
    private String AppName;
    private String AppPackageName;
    private String AppVersion;


    public SearchItemBean(Bitmap appIcon, String appName, String appPackageName, String appVersion) {
        AppIcon = appIcon;
        AppName = appName;
        AppPackageName = appPackageName;
        AppVersion = appVersion;
    }

    public Bitmap getAppIcon() {
        return AppIcon;
    }

    public void setAppIcon(Bitmap appIcon) {
        AppIcon = appIcon;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getAppPackageName() {
        return AppPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        AppPackageName = appPackageName;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }
}
