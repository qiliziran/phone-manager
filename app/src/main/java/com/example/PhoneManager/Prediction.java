package com.example.PhoneManager;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

import static com.example.PhoneManager.MainActivity.getApplicationNameByPackageName;

public class Prediction {
    public void prediction(Context context) {
        GetData appdata = new GetData(context);
        long start_time = appdata.getStartTime() - 86400000;
        long end_time = appdata.getStartTime();

        List<String> appNames1 = new ArrayList();
        List<int[]> appLaunchCounts1 = new ArrayList();
        List<String> appNames2 = new ArrayList();
        List<int[]> appLaunchCounts2 = new ArrayList();
        List<String> appNames3 = new ArrayList();
        List<int[]> appLaunchCounts3 = new ArrayList();
        List<String> appNames4 = new ArrayList();
        List<int[]> appLaunchCounts4 = new ArrayList();
        List<String> appNames5 = new ArrayList();
        List<int[]> appLaunchCounts5 = new ArrayList();
        HashMap<String, AppUsageInfo> datas1 = appdata.getUsageStatistics11(start_time,end_time,context);
        HashMap<String, AppUsageInfo> datas2 = appdata.getUsageStatistics11(start_time-1*24*60*60*1000,end_time-1*24*60*60*1000,context);
        HashMap<String, AppUsageInfo> datas3 = appdata.getUsageStatistics11(start_time-2*24*60*60*1000,end_time-2*24*60*60*1000,context);
        HashMap<String, AppUsageInfo> datas4 = appdata.getUsageStatistics11(start_time-3*24*60*60*1000,end_time-3*24*60*60*1000,context);
        HashMap<String, AppUsageInfo> datas5 = appdata.getUsageStatistics11(start_time-4*24*60*60*1000,end_time-4*24*60*60*1000,context);

        for (Map.Entry<String, AppUsageInfo> entry : datas1.entrySet()) {
            String appName = getApplicationNameByPackageName(context, entry.getKey());
            appNames1.add(appName);
            Log.e("liu", "添加APP：" + appName);
//            Log.e("liu", "长度 " + appNames.get(i));
//            Log.e("liu", (Arrays.toString(appLaunchCounts.get(i))));
            int[] appLaunchCount = new int[24];
            for (int i = 0; i < entry.getValue().getEachHourLaunchCounts().length - 1; i++) {
                appLaunchCount[i] = entry.getValue().EachHourLaunchCounts[i];
            }
            Log.e("liu", Arrays.toString(appLaunchCount));
            appLaunchCounts1.add(appLaunchCount);
        }

        for (Map.Entry<String, AppUsageInfo> entry : datas2.entrySet()) {
            String appName = getApplicationNameByPackageName(context, entry.getKey());
            appNames2.add(appName);
            int[] appLaunchCount = new int[24];
            for (int i = 0; i < entry.getValue().getEachHourLaunchCounts().length - 1; i++) {
                appLaunchCount[i] = entry.getValue().EachHourLaunchCounts[i];
            }
            appLaunchCounts2.add(appLaunchCount);
        }

        for (Map.Entry<String, AppUsageInfo> entry : datas3.entrySet()) {
            String appName = getApplicationNameByPackageName(context, entry.getKey());
            appNames3.add(appName);
            int[] appLaunchCount = new int[24];
            for (int i = 0; i < entry.getValue().getEachHourLaunchCounts().length - 1; i++) {
                appLaunchCount[i] = entry.getValue().EachHourLaunchCounts[i];
            }
            appLaunchCounts3.add(appLaunchCount);
        }

        for (Map.Entry<String, AppUsageInfo> entry : datas4.entrySet()) {
            String appName = getApplicationNameByPackageName(context, entry.getKey());
            appNames4.add(appName);
            int[] appLaunchCount = new int[24];
            for (int i = 0; i < entry.getValue().getEachHourLaunchCounts().length - 1; i++) {
                appLaunchCount[i] = entry.getValue().EachHourLaunchCounts[i];
            }
            appLaunchCounts4.add(appLaunchCount);
        }

        for (Map.Entry<String, AppUsageInfo> entry : datas5.entrySet()) {
            String appName = getApplicationNameByPackageName(context, entry.getKey());
            appNames5.add(appName);
            int[] appLaunchCount = new int[24];
            for (int i = 0; i < entry.getValue().getEachHourLaunchCounts().length - 1; i++) {
                appLaunchCount[i] = entry.getValue().EachHourLaunchCounts[i];
            }
            appLaunchCounts5.add(appLaunchCount);
        }

//        训练
        int appCount = appNames1.size();
        Log.e("liu", "APP数量" + appCount);
        List<Classifier<String, String>> bayesList = new ArrayList();
        for (int i = 0; i < appCount; i++) {
            Classifier<String, String> bayes = new BayesClassifier<String, String>();
            bayesList.add(bayes);
        }

        for (int i = 0; i < appCount; i++) {
            String[] features = new String[24];
            String[] categories = new String[24];
            for (int j = 0; j < 24; j++) {
                features[j] = String.valueOf(j);
                if (appLaunchCounts1.get(i)[j] > 0) {
                    categories[j] = "y";
                }
                else {
                    categories[j] = "n";
                }
            }
            Log.e("liu", Arrays.toString(categories));
            for (int j = 0; j < 24; j++) {
                bayesList.get(i).learn(categories[j], Arrays.asList(features[j]));
            }
        }

        for (int i = 0; i < appCount; i++) {
            String[] features = new String[24];
            String[] categories = new String[24];
            for (int j = 0; j < 24; j++) {
                features[j] = String.valueOf(j);
                if (appLaunchCounts2.get(i)[j] > 0) {
                    categories[j] = "y";
                }
                else {
                    categories[j] = "n";
                }
            }
            Log.e("liu", Arrays.toString(categories));
            for (int j = 0; j < 24; j++) {
                bayesList.get(i).learn(categories[j], Arrays.asList(features[j]));
            }
        }

        for (int i = 0; i < appCount; i++) {
            String[] features = new String[24];
            String[] categories = new String[24];
            for (int j = 0; j < 24; j++) {
                features[j] = String.valueOf(j);
                if (appLaunchCounts3.get(i)[j] > 0) {
                    categories[j] = "y";
                }
                else {
                    categories[j] = "n";
                }
            }
            Log.e("liu", Arrays.toString(categories));
            for (int j = 0; j < 24; j++) {
                bayesList.get(i).learn(categories[j], Arrays.asList(features[j]));
            }
        }

        for (int i = 0; i < appCount; i++) {
            String[] features = new String[24];
            String[] categories = new String[24];
            for (int j = 0; j < 24; j++) {
                features[j] = String.valueOf(j);
                if (appLaunchCounts4.get(i)[j] > 0) {
                    categories[j] = "y";
                }
                else {
                    categories[j] = "n";
                }
            }
            Log.e("liu", Arrays.toString(categories));
            for (int j = 0; j < 24; j++) {
                bayesList.get(i).learn(categories[j], Arrays.asList(features[j]));
            }
        }

        for (int i = 0; i < appCount; i++) {
            String[] features = new String[24];
            String[] categories = new String[24];
            for (int j = 0; j < 24; j++) {
                features[j] = String.valueOf(j);
                if (appLaunchCounts5.get(i)[j] > 0) {
                    categories[j] = "y";
                }
                else {
                    categories[j] = "n";
                }
            }
            Log.e("liu", Arrays.toString(categories));
            for (int j = 0; j < 24; j++) {
                bayesList.get(i).learn(categories[j], Arrays.asList(features[j]));
            }
        }
        Log.e("liu", "tag1");



//        预测
        List<String> hitApp = new ArrayList();
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        Log.e("liu", "当前小时：" + hour);
        for (int i = 0; i < appNames1.size(); i++) {
            String result = bayesList.get(i).classify(Arrays.asList(String.valueOf(hour))).getCategory();
            // will output "positive"
            if (result == "y") {
                Log.e("liu", appNames1.get(i) + " 命中");
                hitApp.add(appNames1.get(i));
            }
            else {
                Log.e("liu", appNames1.get(i) + " 未命中");
            }
        }

        // 输出
        if (hitApp.size() == 0) {
            Log.e("liu", "预测APP个数为0");
        }
        else {
            for (String appName : hitApp) {
                Log.e("liu", "预测：" + appName);
            }
        }
    }

    public void training(String[] categories, String[] features) {
        // Create a new bayes classifier with string categories and string features.
        Classifier<String, String> bayes = new BayesClassifier<String, String>();

// Learn by classifying examples.
// New categories can be added on the fly, when they are first used.
// A classification consists of a category and a list of features
// that resulted in the classification in that category.
        for (int i = 0; i < categories.length; i++)
            bayes.learn(categories[i], Arrays.asList(features[i]));

// Here are two unknown sentences to classify.

        String[] testStrings = {"0", "1", "2"};
        for (int i = 0; i < testStrings.length; i++) {
            String[] unknownText1 = testStrings[i].split("\\s");

            String string = bayes.classify(Arrays.asList(unknownText1)).getCategory();
            // will output "positive"
            Log.e("liu", string);
        }
        String[] unknownText1 = "hello world".split("\\s");
// Get more detailed classification result.
        ((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1));

// Change the memory capacity. New learned classifications (using
// the learn method) are stored in a queue with the size given
// here and used to classify unknown sentences.
        bayes.setMemoryCapacity(500);// Create a new bayes classifier with string categories and string features.
    }
}
