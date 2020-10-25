package com.example.myapplication02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
//import com.example.appmanager.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;

public class ApplicationListActivity extends AppCompatActivity {
    private List<AppInfo> appInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);

        initAppInfo();
        AppInfoAdapter adapter = new AppInfoAdapter(this, R.layout.app_info_item, appInfoList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }
    private void initAppInfo() {
        AppInfoProvider appInfoProvider = new AppInfoProvider(this);
        appInfoList =  appInfoProvider.getAllApps();
        for (int i = 0; i < appInfoList.size(); i++) {
//            if (appInfoList.get(i).getIcon().)
//            Log.d("myDebug", appInfoList.get(i).getAppName());
        }
    }

}
