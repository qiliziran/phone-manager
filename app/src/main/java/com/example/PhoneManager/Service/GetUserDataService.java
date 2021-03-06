package com.example.PhoneManager.Service;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.example.PhoneManager.AppInfoProvider;
import com.example.PhoneManager.DataBase.UserData;
import com.example.PhoneManager.GetData;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetUserDataService extends Service {

    public static final String TAG = GetUserDataService.class.getSimpleName();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    private UsageStatsManager mUsmManager;
    public GetUserDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //后台使用queryUsageStats获取app名、最后一次启动时间、总启动次数和总运行时间
        mUsmManager = getUsageStatsManager();
        try {
            printCurrentUsageStatus(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //后台使用UsageEvents获取app名、第一次运行时间，最后一次运行时间、总启动次数和总运行时间
        //前2小时
        GetData getdata = new GetData(this);
        long end_time = System.currentTimeMillis();
//        long start_time = end_time - hour_in_mil;
        long start_time = getStartTime();
        getdata.SaveToDatabase(start_time,end_time,this);
        //将获取app的图标存进数据库的功能封装成service
        AppInfoProvider app = new AppInfoProvider(this);
        app.getAllAppUsage(this);

        Log.d(TAG, "后台获取数据成功！");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("ResourceType")
    private UsageStatsManager getUsageStatsManager() {
        UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
        return usm;
    }

    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        // 一年前的今天
        calendar.add(Calendar.DATE, -3);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

//        List<UsageStats> usageStatsList = mUsmManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        return usageStatsList;
    }

    public  void printUsageStats(Context context, List<UsageStats> usageStatsList)throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{

        DataSupport.deleteAll(UserData.class);
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        List<com.example.PhoneManager.DataBase.UserData> UserdataList = new ArrayList<com.example.PhoneManager.DataBase.UserData>();
        for (int i=0;i<usageStatsList.size();i++){
            com.example.PhoneManager.DataBase.UserData uds = new UserData();//切不可在循环外，否则UserdataList里只有一条内容！
            uds.setAppName(getApplicationNameByPackageName(context, usageStatsList.get(i).getPackageName()));
            uds.setTotalRunningTime(usageStatsList.get(i).getTotalTimeInForeground());
            String t=format.format(new Date(usageStatsList.get(i).getLastTimeUsed()));
            //设置最后一次运行时间
            uds.setLastRunningTime(t);
//            Field field = list.get(i).getClass().getDeclaredField("mLaunchCount");
            //设置总运行次数（关机后清零！）
            uds.setTotalLaunchCount(usageStatsList.get(i).getClass().getDeclaredField("mLaunchCount").getInt(usageStatsList.get(i)));
            UserdataList.add(i,uds);
//            Log.d(TAG, "Pkg: " + uds.getAppName() +  "\t" + "ForegroundTime: "
//                    + uds.getTotalRunningTime()) ;
//            Log.d(TAG, "应用程序名: " +getApplicationNameByPackageName(context,uds.getAppName())) ;

        }
//        Log.d(TAG, "总个数: "+UserdataList.size());
        DataSupport.saveAll(UserdataList);

    }




    public  void printCurrentUsageStatus(Context context) throws NoSuchFieldException, IllegalAccessException {
        printUsageStats(context,getUsageStatsList(context));
    }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
    /**
     * 根据包名获取应用程序名
     * 参数：（上下文，包名）
     */
    public static String getApplicationNameByPackageName(Context context, String packageName) {

        PackageManager pm = context.getPackageManager();
        String Name ;
        try {
            Name=pm.getApplicationLabel(pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Name = "" ;
        }
        return Name;
    }


    /**
     * 获取今天开始时间
     */
    private Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }


}
