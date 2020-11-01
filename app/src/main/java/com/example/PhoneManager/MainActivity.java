package com.example.PhoneManager;
//import com.android.setting.fuelaguge.BatteryStatsHelper
import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import com.example.PhoneManager.DataBase.UserData;

import com.example.PhoneManager.Service.GetUserDataService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.PhoneManager.Network.Network;
import org.litepal.LitePal;

import java.util.Calendar;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                LitePal.getDatabase();
        //启动 后台获取用户app数据 服务
//        Intent StartIntent = new Intent(MainActivity.this, GetUserDataService.class);
//        startService(StartIntent);

        //开启SQLiteStudio数据库调试服务
        SQLiteStudioService.instance().start(this);

//        Check if permission enabled
        if (GetUserDataService.getUsageStatsList(this).isEmpty()){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        //获取应用详细使用记录
//        long hour_in_mil = 1000*60*60; // In Milliseconds
//        long end_time = System.currentTimeMillis();
//        long start_time = end_time - hour_in_mil;
//        UsageEvent UE = new UsageEvent(hour_in_mil,end_time,start_time);
//        UE.getUsageStatistics(MainActivity.this);



//        this.getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


}
