package com.example.PhoneManager.BottomFragments.me;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.PhoneManager.Login_Register.LoginActivity;
import com.example.PhoneManager.Login_Register.RegisterActivity;
import com.example.PhoneManager.MainActivity;
import com.example.PhoneManager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MeFragment extends Fragment {

    private MeViewModel meViewModel;
    private Button loginout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        meViewModel =
                ViewModelProviders.of(this).get(MeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        meViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //退出登录监听事件
        loginout = root.findViewById(R.id.loginout);

//        Calendar beginCal = Calendar.getInstance();
//        beginCal.add(Calendar.HOUR_OF_DAY, -1);
//        Calendar endCal = Calendar.getInstance();
//        UsageStatsManager manager=(UsageStatsManager)getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
//        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, beginCal.getTimeInMillis(),endCal.getTimeInMillis());
//        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//        Log.d("TAG", "获取的数据为："+stats.size());
//        for (int i = 0; i < stats.size(); i++) {
//            Log.d("TAG", "Name: " + stats.get(i).getPackageName());
//            Log.d("TAG", "First: " + format.format(new Date(stats.get(i).getFirstTimeStamp())));
//            Log.d("TAG", "Last: " + format.format(new Date(stats.get(i).getLastTimeUsed())));
//
//        }

        setListeners();
        return root;
    }
    //监听器
    private void setListeners() {
        OnClick onClick = new OnClick();
        loginout.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginout:
                    SharedPreferences.Editor editer = getActivity().getSharedPreferences("userdata", MODE_PRIVATE).edit();
                    editer.putBoolean("loginstate", false);
                    editer.apply();
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                    startActivity(intent1);
                    break;
            }
        }
    }
}