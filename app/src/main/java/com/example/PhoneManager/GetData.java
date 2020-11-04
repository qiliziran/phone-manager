package com.example.PhoneManager;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.PhoneManager.Service.GetUserDataService.getApplicationNameByPackageName;

public class GetData {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void getUsageStatistics(long start_time, long end_time,Context context) {

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
                if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED ||
                        currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                    //  allEvents.add(currentEvent);
                    String key = currentEvent.getPackageName();
                    if (map.get(key) == null) {
                        map.put(key, new AppUsageInfo(key));
                        sameEvents.put(key,new ArrayList<UsageEvents.Event>());
                    }
                    sameEvents.get(key).add(currentEvent);
                }
            }

            // Traverse through each app data which is grouped together and count launch, calculate duration
            for (Map.Entry<String,List<UsageEvents.Event>> entry : sameEvents.entrySet()) {
                int totalEvents = entry.getValue().size();
                if (totalEvents > 1) {
                    for (int i = 0; i < totalEvents - 1; i++) {
                        UsageEvents.Event E0 = entry.getValue().get(i);
                        UsageEvents.Event E1 = entry.getValue().get(i + 1);

                        if (E1.getEventType() == 1 || E0.getEventType() == 1) {
                            map.get(E1.getPackageName()).launchCount++;
                        }

                        if (E0.getEventType() == 1 && E1.getEventType() == 2) {
                            long diff = E1.getTimeStamp() - E0.getTimeStamp();
                            map.get(E0.getPackageName()).timeInForeground += diff;
                        }
                    }
                }

                // If First eventtype is ACTIVITY_PAUSED then added the difference of start_time and Event occuring time because the application is already running.
                if (entry.getValue().get(0).getEventType() == 2) {
                    long diff = entry.getValue().get(0).getTimeStamp() - start_time;
                    map.get(entry.getValue().get(0).getPackageName()).timeInForeground += diff;
                }

                // If Last eventtype is ACTIVITY_RESUMED then added the difference of end_time and Event occuring time because the application is still running .
                if (entry.getValue().get(totalEvents - 1).getEventType() == 1) {
                    long diff = end_time - entry.getValue().get(totalEvents - 1).getTimeStamp();
                    map.get(entry.getValue().get(totalEvents - 1).getPackageName()).timeInForeground += diff;
                }
            }
            //输出使用情况：
            for (Map.Entry<String, AppUsageInfo> entry : map.entrySet()) {
                Log.d("TAG", "app名称："+getApplicationNameByPackageName(context,entry.getKey())+"\t"
                        +"app启动次数： "+entry.getValue().launchCount+"\t"
                                +"app运行时间： "+entry.getValue().timeInForeground);
//                System.out.println("key= "  + " and value= " + entry.getValue());
            }

//            smallInfoList = new ArrayList<>(map.values());
//
//            // Concatenating data to show in a text view. You may do according to your requirement
//            for (AppUsageInfo appUsageInfo : smallInfoList)
//            {
//                // Do according to your requirement
//                strMsg = strMsg.concat(appUsageInfo.packageName + " : " + appUsageInfo.launchCount + "\n\n");
//            }
//
//            TextView tvMsg = findViewById(R.id.MA_TvMsg);
//            tvMsg.setText(strMsg);

        } else {
            Toast.makeText(context, "Sorry...", Toast.LENGTH_SHORT).show();
        }

    }
}
class AppUsageInfo {
    Drawable appIcon; // You may add get this usage data also, if you wish.
    String appName, packageName;
    long timeInForeground;
    int launchCount;

    AppUsageInfo(String pName) {
        this.packageName=pName;
    }
}
