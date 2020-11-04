package com.example.PhoneManager.BottomFragments.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.PhoneManager.MyDatabaseHelper;
import com.example.PhoneManager.R;

public class ChartFragment extends Fragment {

    private ChartViewModel chartViewModel;
    private MyDatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chartViewModel =
                ViewModelProviders.of(this).get(ChartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        dbHelper = new MyDatabaseHelper(getContext(), "AppInfo.db", null, 1);
        Button createDatabase = (Button) root.findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

//        Intent intent= new Intent();
//        intent.setClass(getContext(), WaveProgressActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getContext().startActivity(intent);

        return root;
    }


}