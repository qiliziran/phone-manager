package com.example.PhoneManager.Network;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.PhoneManager.MainActivity;

import static android.content.Context.NETWORK_STATS_SERVICE;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Network {
    private Context context;
    public static final String TAG = Network.class.getSimpleName();
    public Network(Context context) {
        this.context = context;
    }

//    NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);
public  int getUidByPackageName(Context context, String packageName) {
    int uid = -1;
    PackageManager packageManager = context.getPackageManager();
    try {
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);

        uid = packageInfo.applicationInfo.uid;
        Log.i(TAG, packageInfo.packageName + " uid:" + uid);


    } catch (PackageManager.NameNotFoundException e) {
    }

    return uid;
}
}
