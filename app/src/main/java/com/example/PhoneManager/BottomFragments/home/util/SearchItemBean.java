package com.example.PhoneManager.BottomFragments.home.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class SearchItemBean {
    private Bitmap AppIcon;
    private String AppName;

    public SearchItemBean(Bitmap appIcon, String appName) {
        AppIcon = appIcon;
        AppName = appName;
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
}
