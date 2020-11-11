package com.example.PhoneManager.BottomFragments.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.PhoneManager.R;

public class AppDetailActivity extends AppCompatActivity {
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        Intent intent = getIntent();
        name = findViewById(R.id.appname);
        name.setText(intent.getStringExtra("appname"));

    }
}