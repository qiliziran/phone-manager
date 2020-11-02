package com.example.PhoneManager.Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.PhoneManager.R;

public class TestMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        SharedPreferences pref = getSharedPreferences("userdata",MODE_PRIVATE);
        String name = pref.getString("username","");
        if(name ==""){
            Log.d("TAG", "onCreate: 无数据，即将跳转到登录页面！");
            Intent intent1 = new Intent(TestMainActivity.this, LoginActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
            startActivity(intent1);
        }
    }
}