package com.example.PhoneManager.DataBase;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.sql.Blob;

public class AppIconData extends DataSupport {
    private String AppName;
    private String AppPackageName;
    private Bitmap AppIcon;

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

    public Bitmap getAppIcon() {
        return AppIcon;
    }

    public void setAppIcon(Bitmap appIcon) {
        AppIcon = appIcon;
    }
}
