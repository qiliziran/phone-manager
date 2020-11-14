package com.example.PhoneManager.BottomFragments.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.PhoneManager.R;
public class HistoryRecordFragment extends Fragment {
    String mContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historyrecord, container, false) ;
        TextView textView = (TextView) view.findViewById(R.id.tv);
        return view;
    }

}

