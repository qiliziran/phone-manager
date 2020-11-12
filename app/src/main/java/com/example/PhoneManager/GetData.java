package com.example.PhoneManager;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.PhoneManager.DataBase.AppIconData;
import com.example.PhoneManager.DataBase.AppIconDataBase;
import com.example.PhoneManager.DataBase.AppUsageData;
import com.example.PhoneManager.DataBase.UserData;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import static com.example.PhoneManager.Service.GetUserDataService.getApplicationNameByPackageName;

public class GetData {

    private Context context;

    public GetData(Context context) {
        this.context = context;
    }
//    private  AppInfoProvider app = new AppInfoProvider(context);
//    private  HashMap<String, AppUsageInfo> iconmap = app.getAllAppUsage();

    /**
     * 获取一定时间范围内的app使用信息
     * @param start_time
     * @param end_time
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HashMap<String, AppUsageInfo> getUsageStatistics(long start_time, long end_time, Context context) {
        UsageEvents.Event currentEvent;
        //  List<UsageEvents.Event> allEvents = new ArrayList<>();
        HashMap<String, AppUsageInfo> map = new HashMap<>();
        HashMap<String, List<UsageEvents.Event>> sameEvents = new HashMap<>();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager)
                context.getSystemService(Context.USAGE_STATS_SERVICE);
        long Todayorigintime = getStartTime();
        if (mUsageStatsManager != null) {
            // Get all apps data from starting time to end time
            UsageEvents usageEvents = mUsageStatsManager.queryEvents(start_time, end_time);

            // Put these data into the map
            while (usageEvents.hasNextEvent()) {
                currentEvent = new UsageEvents.Event();
                usageEvents.getNextEvent(currentEvent);
                //ACTIVITY_RESUMED表示进入前台，ACTIVITY_PAUSED表示进入后台
                if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED ||
                        currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                    //  allEvents.add(currentEvent);
                    String key = currentEvent.getPackageName();
                    //判断是否是系统应用程序
                    if(!SystemAppfilter(key)){
                        if (map.get(key) == null) {
                            map.put(key, new AppUsageInfo(key));
                            sameEvents.put(key,new ArrayList<UsageEvents.Event>());
                        }
                        sameEvents.get(key).add(currentEvent);
                    }

                }
            }

            // 对单个app的所有事件进行整理，统计相关信息
            boolean firstrun=true;
            for (Map.Entry<String,List<UsageEvents.Event>> entry : sameEvents.entrySet()) {
                int totalEvents = entry.getValue().size();
                //关键！必须先将其置0，否则后面会出现Null与数字相加，报错
                map.get(entry.getKey()).timeInForeground = 0;
                //初始化今日每个小时的运行时间和启动次数
                map.get(entry.getKey()).EachHourLaunchCounts = new int[24];
                map.get(entry.getKey()).EachHourLaunchCounts[NowHour()+1]=-1;
                map.get(entry.getKey()).EachHourRunningTimes = new long[24];
                map.get(entry.getKey()).EachHourRunningTimes[NowHour()+1] = -1;
                if (totalEvents > 1) {
                    for (int i = 0; i < totalEvents - 1; i++) {
                        UsageEvents.Event E0 = entry.getValue().get(i);
                        UsageEvents.Event E1 = entry.getValue().get(i + 1);

//                        if (E1.getEventType() == 1 || E0.getEventType() == 1) {
//                            map.get(E1.getPackageName()).launchCount++;
//                        }
                        if (E0.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                            map.get(E0.getPackageName()).launchCount++;
                            map.get(E0.getPackageName()).EachHourLaunchCounts[Hour(E0.getTimeStamp())]++;
                            //设置第一次运行时间
                            if(firstrun){
                                map.get(E0.getPackageName()).FirstRunningTime=E0.getTimeStamp();
//                                Log.d("TAG", getApplicationNameByPackageName(context,entry.getKey())
//                                        +"的第一次运行时间为："+LongToString_Time(E0.getTimeStamp())
//                                );
                                firstrun = false;
                            }
                            //设置最后一次运行时间
                            map.get(E0.getPackageName()).LastRunningTime=E0.getTimeStamp();
                            map.get(E0.getPackageName()).LastRuntime =(int) (map.get(E0.getPackageName()).LastRunningTime-Todayorigintime);
                        }
                        if(E0.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED){
                            map.get(E0.getPackageName()).LastBackTime=E0.getTimeStamp();
                        }
                        if (E0.getEventType() == 1 && E1.getEventType() == 2) {
                            long diff = E1.getTimeStamp() - E0.getTimeStamp();
                            int Foregroundtime = (int)diff+map.get(E0.getPackageName()).timeInForeground.intValue();
                            map.get(E0.getPackageName()).timeInForeground =(int)Foregroundtime;
                            //进入前台和进入后台在同一小时内
                            if(Hour(E0.getTimeStamp())==Hour(E1.getTimeStamp())){
                                map.get(E0.getPackageName()).EachHourRunningTimes[Hour(E0.getTimeStamp())] +=diff;
                            }else{
                                long temp = 3600000;
                                map.get(E0.getPackageName()).EachHourRunningTimes[Hour(E0.getTimeStamp())]+= 3600000-(E0.getTimeStamp()%3600000);
                            }    map.get(E0.getPackageName()).EachHourRunningTimes[Hour(E1.getTimeStamp())]+= E1.getTimeStamp()%3600000;
                        }
                    }
                    //统计该应用最后一个事件是否是进入前台，如果是，将启动次数加1
                    if (entry.getValue().get(totalEvents-1).getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                        map.get(entry.getValue().get(totalEvents-1).getPackageName()).launchCount++;
                    }
                }
//                map.get(entry.getValue()).LastRuntime =(int) (map.get(entry.getValue()).LastRunningTime%86400000);

                //关键！必须将firstrun重置，否则只能获取第一个应用的第一次启动时间
                firstrun = true;
                // 如果第一次事件是进入后台，则统计开始的时间戳---第一次事件的时间戳，这段时间也算是前台运行时间，需要统计到
                if (entry.getValue().get(0).getEventType() == 2) {
                    long diff = entry.getValue().get(0).getTimeStamp() - start_time;
                    map.get(entry.getKey()).EachHourRunningTimes[Hour(entry.getValue().get(0).getTimeStamp())] +=diff;
                    int Foregroundtime1 = (int)diff+map.get(entry.getKey()).timeInForeground;
                    map.get(entry.getKey()).timeInForeground =(int)Foregroundtime1;

                }

                // 如果最后一次事件是进入前台，则最后一次事件的时间戳---统计结束的时间戳，这段时间也算是前台运行时间，需要统计到
                if (entry.getValue().get(totalEvents - 1).getEventType() == 1) {
                    long diff = end_time - entry.getValue().get(totalEvents - 1).getTimeStamp();
                    map.get(entry.getKey()).EachHourRunningTimes[Hour(entry.getValue().get(totalEvents - 1).getTimeStamp())] +=diff;
                    int Foregroundtime2 = (int)diff+map.get(entry.getKey()).timeInForeground;
                    map.get(entry.getKey()).timeInForeground =(int)Foregroundtime2;
                }
            }
            map = sortBytimeInForeground(map);
            return map;
        } else {
            return null;
        }

    }


    /**
     * 将获取的app使用信息保存到数据库
     * @param start_time
     * @param end_time
     * @param context
     */
    public void SaveToDatabase(long start_time,long end_time,Context context){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        HashMap<String, AppUsageInfo> map =getUsageStatistics(start_time,end_time,context);
                //将app使用信息存进数据库
        DataSupport.deleteAll(AppUsageData.class);
        List<AppUsageData> AppUsageDataList = new ArrayList<AppUsageData>();
        int count=0;
        //输出使用情况：
        for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
            AppUsageData AppUs = new AppUsageData();
            AppUs.setAppName(getApplicationNameByPackageName(context,entry.getKey()));
            AppUs.setFirstRunningTime(format.format(new Date(entry.getValue().FirstRunningTime)));
            AppUs.setLastRunningTime(format.format(new Date(entry.getValue().LastRunningTime)));
            AppUs.setLaunchCount(entry.getValue().launchCount);
            AppUs.setTimeInForeground(entry.getValue().timeInForeground);
            AppUsageDataList.add(count++,AppUs);
            DataSupport.saveAll(AppUsageDataList);
//            Log.d("TAG", "app包名："+entry.getKey()+"\t"
//                    +"app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
//                    +"app启动次数： "+entry.getValue().launchCount+"\t"
//                    +"app运行时间： "+entry.getValue().timeInForeground);
        }
    }


    /**
     * 获取今日运行时间最长的几个app
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<AppUsageInfo> GetTopApps(Context context) {
        long end_time = System.currentTimeMillis();
        long start_time = getStartTime();
        HashMap<String, AppUsageInfo> map =getUsageStatistics(start_time,end_time,context);
//        AppInfoProvider app = new AppInfoProvider(context);
//        HashMap<String, AppUsageInfo> iconmap = app.getAllAppUsage();

        //统计运行时间最长的前四个应用
        long AllRunningTime = 0;
//        long TopRunningTime = 0;
        long TopTopRunningTimePercentage = 0;
        int count=0;
        List<AppUsageInfo> TopApps = new  ArrayList<AppUsageInfo>();
        for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
            AllRunningTime+=entry.getValue().timeInForeground;
        }
        for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {

            float percentage = (float)(entry.getValue().timeInForeground*100/AllRunningTime);
//            Log.d("TAG", "app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
//                    +"app运行时间： "+percentage+"\t"
//                    +"app真运行时间： "+entry.getValue().timeInForeground);

            if(count<4){
                //获取app图标
//                entry.getValue().appIcon=iconmap.get(entry.getKey()).getAppIcon();


//                Log.d("TAG", "app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
//                        +"app运行时间： "+percentage+"\t"
//                        +"app真运行时间： "+entry.getValue().timeInForeground);
                entry.getValue().setTimeInForegroundPercentage(percentage);
                entry.getValue().setAppName(getApplicationNameByPackageName(context,entry.getKey()));
                TopTopRunningTimePercentage += entry.getValue().timeInForegroundPercentage;
                if (entry.getValue().timeInForegroundPercentage!=0.0){
                    TopApps.add(entry.getValue());
                }
                for (int i = 0; i <24 ; i++) {
                    Log.d(entry.getValue().appName+"运行时间", i+"点:"+entry.getValue().EachHourRunningTimes[i]);
                }
                for (int i = 0; i <24 ; i++) {
                    Log.d(entry.getValue().appName+"启动次数", i+"点:"+entry.getValue().EachHourLaunchCounts[i]);
                }
                count++;
            }
        }

        //将其他所有应用使用情况导入TopApps中
        AppUsageInfo others = new AppUsageInfo("其他");
        others.setAppName("其他");
        others.setTimeInForegroundPercentage(100-TopTopRunningTimePercentage);
        Log.d("TAG", "app名称："+others.appName+"\t"
                +"app运行时间： "+others.timeInForegroundPercentage+"\t"
                +"app真运行时间： "+others.timeInForeground);
        TopApps.add(others);
//            //输出使用情况：
//            for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
//
//                Log.d("TAG", "app包名："+entry.getKey()+"\t"
//                        +"app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
//                        +"app启动次数： "+entry.getValue().launchCount+"\t"
//                        +"app运行时间： "+entry.getValue().timeInForeground);
//            }
        return TopApps;
    }

    /**
     * 统计最近使用的4个app
     * 记得对events进行倒序遍历！
     * @param context
     */
    public List<AppUsageInfo> GetLastestApps(Context context){
        long end_time = System.currentTimeMillis();
        long start_time = getStartTime();
        int count = 0;
        List<AppIconData> AppIconDataList = DataSupport.findAll(AppIconData.class);
        AppIconDataBase aid = new AppIconDataBase(context);
        SQLiteDatabase sd = aid.getWritableDatabase();
//        AppInfoProvider app = new AppInfoProvider(context);
//        HashMap<String, AppUsageInfo> iconmap = app.getAllAppUsage(context);
        HashMap<String, AppUsageInfo> iconmap = GetAppIcons();

        List<AppUsageInfo> LastestAppsList = new  ArrayList<AppUsageInfo>();
        HashMap<String, AppUsageInfo> map = getUsageStatistics(start_time,end_time,context);
        //按照最后一次运行时间排序
        map = sortByLastRunningTime(map);
        for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
//            Log.d("TAG", "app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
//                    +"app最后运行时间： "+entry.getValue().LastRuntime
//                    +"app包名： "+entry.getValue().packageName);
            //历史性BUG！！！iconmap.containsKey(entry.getKey())这句话用于判断最近的应用中没有图标的情况
            if(count<4&&iconmap.containsKey(entry.getKey())){
                //获取app图标
                String[] column = {"appicon"};//你要的数据
                String condition="packagename=?";
                String[] selectionArgs={entry.getKey()};//具体的条件,注意要对应条件字段
                Cursor cursor=sd.query("Appicon", column, condition,selectionArgs, null, null, null, null);

                if (cursor.moveToFirst())
                {
                    byte[] b = cursor.getBlob(cursor.getColumnIndex("appicon"));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
                    entry.getValue().appIcon=bitmap;
                }
                cursor.close();

                //将获取的数据转换成drawable
//                entry.getValue().appIcon=getBitmapFromDrawable(context.getResources().getDrawable(R.drawable.launch_icon));
//                entry.getValue().appIcon=iconmap.get(entry.getKey()).getAppIcon();
//                Log.d("TAG", "app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
//                        +"app最后运行时间： "+LongToString_Time2(entry.getValue().LastRunningTime)
//                        +"app包名： "+entry.getValue().packageName);
                entry.getValue().setAppName(getApplicationNameByPackageName(context,entry.getKey()));
                LastestAppsList.add(entry.getValue());
//                Log.d("TAG", "count: "+count);
                count++;
            }
//            if(count==4) break;
        }
        return LastestAppsList;
    }
    /**
     * HashMap按总运行时间降序排序
     * @param hm
     * @return
     */
    public  HashMap<String, AppUsageInfo> sortBytimeInForeground(HashMap<String, AppUsageInfo> hm)
    {
        // HashMap的entry放到List中
        List<Map.Entry<String, AppUsageInfo> > list =
                new LinkedList<Map.Entry<String, AppUsageInfo> >(hm.entrySet());

        //  对List按entry的value排序
        Collections.sort(list, new Comparator<Map.Entry<String, AppUsageInfo> >() {
            public int compare(Map.Entry<String, AppUsageInfo> o1,
                               Map.Entry<String, AppUsageInfo> o2)
            {
                return (o2.getValue().timeInForeground).compareTo(o1.getValue().timeInForeground);
            }
        });

        // 将排序后的元素放到LinkedHashMap中
        HashMap<String, AppUsageInfo> temp = new LinkedHashMap<String, AppUsageInfo>();
        for (Map.Entry<String, AppUsageInfo> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * HashMap按最后一次运行时间降序排序
     * @param hm
     * @return
     */
    public  HashMap<String, AppUsageInfo> sortByLastRunningTime(HashMap<String, AppUsageInfo> hm)
    {
        // HashMap的entry放到List中
        List<Map.Entry<String, AppUsageInfo> > list =
                new LinkedList<Map.Entry<String, AppUsageInfo> >(hm.entrySet());

        //  对List按entry的value排序
        Collections.sort(list, new Comparator<Map.Entry<String, AppUsageInfo> >() {
            public int compare(Map.Entry<String, AppUsageInfo> o1,
                               Map.Entry<String, AppUsageInfo> o2)
            {
                return (o2.getValue().LastRuntime).compareTo(o1.getValue().LastRuntime);
            }
        });

        // 将排序后的元素放到LinkedHashMap中
        HashMap<String, AppUsageInfo> temp = new LinkedHashMap<String, AppUsageInfo>();
        for (Map.Entry<String, AppUsageInfo> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * 根据包名或者应用名，判断是否为系统应用程序
     * 是则返回true；否则返回false
     * @param packname
     * @return
     */
    boolean SystemAppfilter(String packname){
        Set<String> sysapps = new HashSet();
        sysapps.add("com.miui.home");
        sysapps.add("系统桌面");
        sysapps.add("设置");
        sysapps.add("com.miui.securitycenter");
        sysapps.add("com.miui.home");
        sysapps.add("com.android.settings");
        sysapps.add("com.android.systemui");
        sysapps.add("com.lbe.security.miui");
//        sysapps.add("com.miui.contentcatcher");
        return sysapps.contains(packname);
    }

    public HashMap<String, AppUsageInfo> GetAppIcons(){
        HashMap<String, AppUsageInfo> appicons = new HashMap<>();
        AppIconDataBase aid = new AppIconDataBase(context);
        SQLiteDatabase sd = aid.getWritableDatabase();
        Cursor c = sd.query("Appicon", null, null, null, null, null, null);
        if(c != null && c.getCount() != 0) {
            while(c.moveToNext()) {
                //获取数据
                String packagename = c.getString(c.getColumnIndex("packagename"));
                if (appicons.get(packagename) == null) {
                    appicons.put(packagename, new AppUsageInfo(packagename));
                }
                byte[] b = c.getBlob(c.getColumnIndex("appicon"));
                //将获取的数据转换成drawable
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
                appicons.get(packagename).setAppIcon(bitmap);
            }
        }
        return appicons;
    }

    /**
     * 获取今天开始时间
     */
    public Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 获取今天结束时间
     */
    private Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }


    /**
     *long类型时间--> xxxx年-xx月-xx日 时：分：秒 类型时间
     */
    public  String LongToString_Time1(long l){
        Date date = new Date(l);
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        String time=sim.format(date);
        return time;
    }

    /**
     *long类型时间-->  时：分：秒 类型时间
     */
    public  String LongToString_Time2(long l){
        Date date = new Date(l);
        SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
        String time=sim.format(date);
        return time;
    }

    /**
     * 将long类型的今日时间转换成具体多少点
     * @param time
     * @return
     */
    public int Hour(long time){
        long todaytime = getStartTime();
        long di = time-todaytime;
        int d = (int)di;
        int hour = d/3600000;
        return hour;
    }

    /**
     * 计算现在是多少点
     * @return
     */
    public int NowHour(){
        long nowtime = System.currentTimeMillis();
        long todaytime = getStartTime();
        long di = nowtime-todaytime;
        int d = (int)di;
        int hour = d/3600000;
        return hour;
    }

    /**
     * 将没有图标的应用转换成这个，
     * @param drawable
     * @return
     */
    static public Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        // 部分应用没有图标，会返回AdaptiveiconDrawable，用这种方式也能转换为Bitmap
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
}


/*
 * app使用信息
 */

