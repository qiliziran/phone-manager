package com.example.PhoneManager.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PhoneManager.Features;
import com.example.PhoneManager.FeaturesAdapter;
import com.example.PhoneManager.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Features> featuresList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        initFeatures();
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        FeaturesAdapter adapter = new FeaturesAdapter(getContext(),featuresList);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void initFeatures() {
        Features applicationList = new Features("应用列表", R.drawable.application_list);
        featuresList.add(applicationList);
        Features battery = new Features("电池情况", R.drawable.battery);
        featuresList.add(battery);
        Features features3 = new Features("features3", R.drawable.features3);
        featuresList.add(features3);
        Features features4 = new Features("features4", R.drawable.features4);
        featuresList.add(features4);
    }
}