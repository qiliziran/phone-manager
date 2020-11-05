/*
 * 获取所有应用程序信息
 * 主要依靠packageManager类
 */
package com.example.PhoneManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.PhoneManager.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppInfoProvider {

    private PackageManager packageManager;
    private Context context;
    //获取一个包管理器
    public AppInfoProvider(Context context){
        this.context = context;
        packageManager = context.getPackageManager();

    }
    /**
     *获取系统中所有应用信息，
     *并将应用软件信息保存到list列表中。
     **/
    public List<AppInfo> getAllApps(){
        HashMap<String, AppUsageInfo> map = new HashMap<>();
        List<AppInfo> list = new ArrayList<AppInfo>();
        AppInfo myAppInfo;
        //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(PackageInfo info:packageInfos){
            myAppInfo = new AppInfo();
            //拿到包名
            String packageName = info.packageName;
            String versionName = info.versionName;
            //拿到应用程序的信息
            ApplicationInfo appInfo = info.applicationInfo;
            //拿到应用程序的图标
            Drawable icon = appInfo.loadIcon(packageManager);
            //拿到应用程序的大小
            //long codesize = packageStats.codeSize;
            //Log.i("info", "-->"+codesize);
            //拿到应用程序的程序名
            String appName = appInfo.loadLabel(packageManager).toString();

            myAppInfo.setPackageName(packageName);
            myAppInfo.setVersionName(versionName);
            myAppInfo.setAppName(appName);
            myAppInfo.setIcon(icon);

            //将app的信息存进

            if(filterApp(appInfo)){
                if (!myAppInfo.getIcon().isVisible()) {
                    Log.d("myDebug", myAppInfo.getAppName());
                    continue;
                }
                myAppInfo.setSystemApp(false);
                list.add(myAppInfo);
            }else{
                myAppInfo.setSystemApp(true);
            }

        }
        return list;

    }
    /**
     * 获取app包名、名称和图标的函数接口
     * 非系统应用程序
     */
    public HashMap<String, AppUsageInfo> getAllAppUsage(){
        HashMap<String, AppUsageInfo> map = new HashMap<>();
        //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(PackageInfo info:packageInfos){
            //拿到包名
            String packageName = info.packageName;
            String versionName = info.versionName;
            //拿到应用程序的信息
            ApplicationInfo appInfo = info.applicationInfo;
            //拿到应用程序的图标
            Drawable icon = appInfo.loadIcon(packageManager);
            //拿到应用程序的大小
            //long codesize = packageStats.codeSize;
            //Log.i("info", "-->"+codesize);
            //拿到应用程序的程序名
            String appName = appInfo.loadLabel(packageManager).toString();

            //将app的信息存进map中
            if(filterApp(appInfo)){
                map.put(packageName, new AppUsageInfo(packageName));
                map.get(packageName).setAppIcon(icon);
                map.get(packageName).setAppName(appName);
                map.get(packageName).setPackageName(packageName);
                //如果应用图标不可见，则设置默认图标
                if (!map.get(packageName).getAppIcon().isVisible()){
                    map.get(packageName).setAppIcon(context.getResources().getDrawable(R.drawable.launch_icon));
                }
            }
        }
        return map;
    }

    /**
     *判断某一个应用程序是不是系统的应用程序，
     *如果不是返回true，否则返回false。
     */
    public boolean filterApp(ApplicationInfo info){
        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
            return true;
        }else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0){//判断是不是系统应用
            return true;
        }
        return false;
    }
}
