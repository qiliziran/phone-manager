package com.example.PhoneManager;
//import com.android.setting.fuelaguge.BatteryStatsHelper
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.example.PhoneManager.DataBase.UserData;

import com.example.PhoneManager.Login_Register.LoginActivity;
import com.example.PhoneManager.Login_Register.TestMainActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断是否处于登录状态
        SharedPreferences pref = getSharedPreferences("userdata",MODE_PRIVATE);
        String name = pref.getString("username","");
        boolean loginstate = pref.getBoolean("loginstate",false);
        if(name ==""||loginstate==false){
            Log.d("TAG", "用户名："+name);
            Log.d("TAG", "登录状态："+loginstate);
            Log.d("TAG", "onCreate: 无数据，即将跳转到登录页面！");
            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
            startActivity(intent1);
        }

        LitePal.getDatabase();
        //启动 后台获取用户app数据 服务
        Intent StartIntent = new Intent(MainActivity.this, GetUserDataService.class);
        startService(StartIntent);

        //开启SQLiteStudio数据库调试服务
        SQLiteStudioService.instance().start(this);

//        Check if permission enabled
        if (GetUserDataService.getUsageStatsList(this).isEmpty()){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        //获取应用详细使用记录
        long hour_in_mil = 1000*60*60; // In Milliseconds
        long end_time = System.currentTimeMillis();
//        long start_time = end_time - hour_in_mil;
        long start_time = getStartTime();
        Log.d(TAG, "起始时间："+LongToString_Time(start_time)+"\t"+"结束时间："+LongToString_Time(end_time));
        //版本一
        UsageEvent UE = new UsageEvent(hour_in_mil,start_time,end_time);
        UE.getUsageStatistics(MainActivity.this);
        //版本二
        GetData appdata = new GetData(MainActivity.this);
//        appdata.GetTopApps(MainActivity.this);
//        appdata.getUsageStatistics(start_time,end_time,MainActivity.this);
        appdata.GetLastestApps(MainActivity.this);

//        this.getSupportActionBar().hide();
        /* 获取底部导航栏视图 */
        BottomNavigationView navView = findViewById(R.id.nav_view);
//        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
//        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /* AppBar配置 */
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        /* 从Fragment获取导航控制器，navGraph定义了导航规则 */
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        /* 为AppBar设置导航控制器，监听导航改变事件，修改标题 */
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        /* 为视图设置导航控制器，即监听视图的点击事件 */
        NavigationUI.setupWithNavController(navView, navController);
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
