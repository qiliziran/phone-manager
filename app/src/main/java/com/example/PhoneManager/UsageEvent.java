package com.example.PhoneManager;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.PhoneManager.Service.GetUserDataService;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.PhoneManager.Service.GetUserDataService.getApplicationNameByPackageName;

public class UsageEvent {
    public static final String TAG = UsageEvent.class.getSimpleName();
    long hour_in_mil;
    long end_time ;
    long start_time ;
    UsageEvents usageEvents;
    public UsageEvent(long hour_in_mil, long end_time, long start_time) {
        this.hour_in_mil = hour_in_mil;
        this.end_time = end_time;
        this.start_time = start_time;
    }

    public static String chargeSecondsToNowTime(String seconds) {

        long time = Long.parseLong(seconds)*1000-8*3600*1000;
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
        return format2.format(new Date(time));

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   public void getUsageStatistics(Context context) {

        UsageEvents.Event currentEvent;
//        //  List<UsageEvents.Event> allEvents = new ArrayList<>();
//        HashMap<String, AppUsageInfo> map = new HashMap<>();
//        HashMap<String, List<UsageEvents.Event>> sameEvents = new HashMap<>();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager)
                context.getSystemService(Context.USAGE_STATS_SERVICE);

        if (mUsageStatsManager != null) {
            // Get all apps data from starting time to end time
            usageEvents = mUsageStatsManager.queryEvents(start_time, end_time);
        }
        while (usageEvents.hasNextEvent()){
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            String date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(
                    new java.util.Date(currentEvent.getTimeStamp()));
            Log.d(TAG, "事件类型： "+currentEvent.getEventType()+"\t"
                    +"事件对应包名： "+currentEvent.getPackageName()+"\t"
                    +"事件对应APP： "+getApplicationNameByPackageName(context,currentEvent.getPackageName())+"\t"
                    +"事件开始的时间： "+date+"\t");
        }

    }

}

