package com.example.PhoneManager;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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

            // Traverse through each app data which is grouped together and count launch, calculate duration
            boolean firstrun=true;
            boolean lastrun=true;
            for (Map.Entry<String,List<UsageEvents.Event>> entry : sameEvents.entrySet()) {
                int totalEvents = entry.getValue().size();
                //关键！必须先将其置0，否则后面会出现Null与数字相加，报错
                map.get(entry.getKey()).timeInForeground = 0;
                if (totalEvents > 1) {
                    for (int i = 0; i < totalEvents - 1; i++) {
                        UsageEvents.Event E0 = entry.getValue().get(i);
                        UsageEvents.Event E1 = entry.getValue().get(i + 1);

//                        if (E1.getEventType() == 1 || E0.getEventType() == 1) {
//                            map.get(E1.getPackageName()).launchCount++;
//                        }
                        if (E0.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                            map.get(E0.getPackageName()).launchCount++;
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
                            map.get(E0.getPackageName()).LastRuntime =(int) (map.get(E0.getPackageName()).LastRunningTime%86400000);
                        }

                        if (E0.getEventType() == 1 && E1.getEventType() == 2) {
                            long diff = E1.getTimeStamp() - E0.getTimeStamp();
                            int Foregroundtime = (int)diff+map.get(E0.getPackageName()).timeInForeground.intValue();
                            map.get(E0.getPackageName()).timeInForeground =(int)Foregroundtime;
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
                // 如果第一次事件是进入前台，则统计开始的时间戳---第一次事件的时间戳，这段时间也算是前台运行时间，需要统计到
                if (entry.getValue().get(0).getEventType() == 2) {
                    long diff = entry.getValue().get(0).getTimeStamp() - start_time;
                    int Foregroundtime1 = (int)diff+map.get(entry.getValue().get(0).getPackageName()).timeInForeground;
                    map.get(entry.getValue().get(0).getPackageName()).timeInForeground =(int)Foregroundtime1;
                }

                // 如果最后一次事件是进入前台，则最后一次事件的时间戳---统计结束的时间戳，这段时间也算是前台运行时间，需要统计到
                if (entry.getValue().get(totalEvents - 1).getEventType() == 1) {
                    long diff = end_time - entry.getValue().get(totalEvents - 1).getTimeStamp();
                    int Foregroundtime2 = (int)diff+map.get(entry.getValue().get(totalEvents - 1).getPackageName()).timeInForeground;
                    map.get(entry.getValue().get(totalEvents - 1).getPackageName()).timeInForeground =(int)Foregroundtime2;
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
        AppInfoProvider app = new AppInfoProvider(context);
        HashMap<String, AppUsageInfo> iconmap = app.getAllAppUsage();

        //统计运行时间最长的前四个应用
        long AllRunningTime=0;
        int count=0;
        List<AppUsageInfo> TopApps = new  ArrayList<AppUsageInfo>();
        for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
            AllRunningTime+=entry.getValue().timeInForeground;
            if(count<4){
                //获取app图标
                entry.getValue().appIcon=iconmap.get(entry.getKey()).getAppIcon();
                Log.d("TAG", "app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
                        +"app运行时间： "+entry.getValue().timeInForeground);
                TopApps.add(entry.getValue());
                count++;
            }
        }


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
    public void GetLastestApps(Context context){
        long end_time = System.currentTimeMillis();
        long start_time = getStartTime();
        int count = 0;
        AppInfoProvider app = new AppInfoProvider(context);
        HashMap<String, AppUsageInfo> iconmap = app.getAllAppUsage();
        List<AppUsageInfo> LastestAppsList = new  ArrayList<AppUsageInfo>();
        HashMap<String, AppUsageInfo> map = getUsageStatistics(start_time,end_time,context);
        map = sortByLastRunningTime(map);
        for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
            if(count<4){
                //获取app图标
                entry.getValue().appIcon=iconmap.get(entry.getKey()).getAppIcon();
                Log.d("TAG", "app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
                        +"app运行时间： "+entry.getValue().timeInForeground);
                LastestAppsList.add(entry.getValue());
                count++;
            }else{
                break;
            }
        }
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
        return sysapps.contains(packname);
    }

    /**
     * 获取今天开始时间
     */
    private Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
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
    public static String LongToString_Time(long l){
        Date date = new Date(l);
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time=sim.format(date);
        return time;
    }
}


/*
 * app使用信息
 */
class AppUsageInfo {
    Drawable appIcon; // You may add get this usage data also, if you wish.
    String appName, packageName;
    Integer timeInForeground;
    Integer LastRuntime;
    int launchCount;
    long FirstRunningTime,LastRunningTime;
    //以包名为形参的构造函数
    AppUsageInfo(String pName) {
        this.packageName=pName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
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
}
