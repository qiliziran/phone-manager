/*
 * 获取所有应用程序信息
 * 主要依靠packageManager类
 */
package com.example.PhoneManager;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.PhoneManager.DataBase.AppIconData;
import com.example.PhoneManager.DataBase.AppIconDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.litepal.crud.DataSupport;

import static com.example.PhoneManager.Service.GetUserDataService.getApplicationNameByPackageName;

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
    public HashMap<String, AppUsageInfo> getAllAppUsage(Context context){

        AppIconDataBase aid = new AppIconDataBase(context);
        SQLiteDatabase sd = aid.getWritableDatabase();

        List<AppIconData> AppIconDataList = new ArrayList<AppIconData>();
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
                Bitmap bicon = getBitmapFromDrawable(icon);
                map.get(packageName).setAppIcon(bicon);
                map.get(packageName).setAppName(appName);
                map.get(packageName).setPackageName(packageName);
                //如果应用图标不可见，则设置默认图标
                if (!icon.isVisible()){
                    Bitmap defaulticon = getBitmapFromDrawable(context.getResources().getDrawable(R.drawable.launch_icon));
                    map.get(packageName).setAppIcon(defaulticon);
                }
            }
        }
        int count=0;
        //输出使用情况：
        for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
            ContentValues values = new ContentValues();
            values.put("packagename",entry.getValue().getPackageName());
            values.put("appicon",BitmapToByte(entry.getValue().getAppIcon()));
            sd.insert("Appicon",null,values);
            values.clear();
//            Log.d("TAG", "app包名："+entry.getKey()+"\t"
//                    +"app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
//                    +"app启动次数： "+entry.getValue().launchCount+"\t"
//                    +"app运行时间： "+entry.getValue().timeInForeground);
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

    static public Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        // 部分应用没有图标，会返回AdaptiveiconDrawable，用这种方式也能转换为Bitmap
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    //将drawable转换成可以用来存储的byte[]类型
    private byte[] BitmapToByte(Bitmap appicon) {
        if(appicon == null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        appicon.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }
}
