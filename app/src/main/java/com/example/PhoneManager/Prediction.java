package com.example.PhoneManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

        List<String> appNames = new ArrayList();
        List<int[]> appLaunchCounts = new ArrayList();

        HashMap<String, AppUsageInfo> datas = appdata.getUsageStatistics11(start_time,end_time,context);
        for (Map.Entry<String, AppUsageInfo> entry : datas.entrySet()) {
            String appName = getApplicationNameByPackageName(context, entry.getKey());
            Log.e("liu", appName);
            appNames.add(appName);
            Log.e("liu", "添加APP：" + appName + " 长度：" + entry.getValue().getEachHourLaunchCounts().length);
//            Log.e("liu", "长度 " + appNames.get(i));
//            Log.e("liu", (Arrays.toString(appLaunchCounts.get(i))));
            int[] appLaunchCount = new int[25];
            for (int i = 0; i < entry.getValue().getEachHourLaunchCounts().length; i++) {
                appLaunchCount[i] = entry.getValue().EachHourLaunchCounts[i];
            }
            Log.e("liu", Arrays.toString(appLaunchCount));
            appLaunchCounts.add(appLaunchCount);
        }

//        训练
        Classifier<String, String> bayes = new BayesClassifier<String, String>();
        for (int i = 0; i < appNames.size(); i++) {
            String[] features = new String[24];
            String[] categories = new String[24];

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
