package com.example.PhoneManager.BottomFragments.me;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
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

import com.example.PhoneManager.AppUsageInfo;
import com.example.PhoneManager.ApplicationListActivity;
import com.example.PhoneManager.GetData;
import com.example.PhoneManager.Login_Register.LoginActivity;
import com.example.PhoneManager.Login_Register.RegisterActivity;
import com.example.PhoneManager.MainActivity;
import com.example.PhoneManager.Prediction;
import com.example.PhoneManager.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.PhoneManager.MainActivity.getApplicationNameByPackageName;

public class MeFragment extends Fragment {

    private MeViewModel meViewModel;
    private TextView meTextView1;
    private TextView meTextView2;
    private TextView meTextView3;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        meViewModel =
                ViewModelProviders.of(this).get(MeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        meTextView1 = root.findViewById(R.id.tv_me_tv1);
        Drawable drawable=getResources().getDrawable(R.drawable.me);
        drawable.setBounds(60,10,120,80);//第一0是距左邊距離，第二0是距上邊距離，30、35分別是長寬
        meTextView1.setCompoundDrawables(drawable,null,null,null);//只放左邊

        meTextView2 = root.findViewById(R.id.tv_me_tv2);
        Drawable drawable2=getResources().getDrawable(R.drawable.me);
        drawable2.setBounds(60,10,120,80);//第一0是距左邊距離，第二0是距上邊距離，30、35分別是長寬
        meTextView2.setCompoundDrawables(drawable2,null,null,null);//只放左邊

        meTextView3 = root.findViewById(R.id.tv_me_tv3);
        Drawable drawable3=getResources().getDrawable(R.drawable.me);
        drawable3.setBounds(60,10,120,80);//第一0是距左邊距離，第二0是距上邊距離，30、35分別是長寬
        meTextView3.setCompoundDrawables(drawable3,null,null,null);//只放左邊



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
//退出登录监听事件
        setListeners();
        return root;
    }
    //监听器
    private void setListeners() {
        OnClick onClick = new OnClick();
        meTextView3.setOnClickListener(onClick);
        meTextView2.setOnClickListener(onClick);
        meTextView1.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_me_tv3:
                    SharedPreferences.Editor editer = getActivity().getSharedPreferences("userdata", MODE_PRIVATE).edit();
                    editer.putBoolean("loginstate", false);
                    editer.apply();
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                    startActivity(intent1);
                    break;
                case R.id.tv_me_tv2:
                    Intent intent = new Intent(getActivity(), ApplicationListActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                    startActivity(intent);
                    break;
                case R.id.tv_me_tv1:
                    Log.e("liu", "开始");


                    Prediction prediction = new Prediction();
                    prediction.prediction(getActivity());
                    Log.e("liu", "结束");
            }
        }
    }
}