package com.example.PhoneManager.BottomFragments.home;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PhoneManager.AppInfo;
import com.example.PhoneManager.AppInfoAdapter;
import com.example.PhoneManager.AppInfoProvider;

import com.example.PhoneManager.AppUsageInfo;
import com.example.PhoneManager.Features;
import com.example.PhoneManager.FeaturesAdapter;
import com.example.PhoneManager.GetData;
import com.example.PhoneManager.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.veken.chartview.bean.PieChartBean;
import com.veken.chartview.view.PieChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.PhoneManager.AppInfoAdapter.getBitmapFromDrawable;

public class HomeFragment extends Fragment {
    private Button btn_predict;
    private HomeViewModel homeViewModel;
    private PieChart mPieChart;
    private List<UsingRecord> lastestappList = new ArrayList<>();
    private List<Features> featuresList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        //获取app的图标名称等数据，在该Fragment中展示
//        AppInfoProvider app = new AppInfoProvider(getContext());
//        List<AppInfo> appInfoList =  app.getAllApps();
//        Bitmap bitmap = getBitmapFromDrawable(appInfoList.get(0).getIcon());


//        initFeatures();
        //加载布局
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
//        //创建一个每行最多两个View的网格布局
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        FeaturesAdapter adapter = new FeaturesAdapter(getContext(),featuresList);
//        recyclerView.setAdapter(adapter);

        /*折线饼状图*/
        //1.初始化组件
        mPieChart = (PieChart) root.findViewById(R.id.mPieChart);

        mPieChart.setUsePercentValues(false); //设置是否使用百分值,默认不显示
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        //是否设置中心文字
        mPieChart.setDrawCenterText(false);
        //绘制中间文字
        SpannableString sp = new SpannableString("个人剖析图");
        mPieChart.setCenterText(sp);
        mPieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        //设置是否为实心图，以及空心时 中间的颜色
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        //设置圆环信息
        mPieChart.setTransparentCircleColor(Color.WHITE);//设置透明环颜色
        mPieChart.setTransparentCircleAlpha(110);//设置透明环的透明度
        mPieChart.setHoleRadius(35f);//设置内圆的大小
        mPieChart.setTransparentCircleRadius(37f);//设置透明环的大小

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        //选中变大
        mPieChart.setHighlightPerTapEnabled(true);

        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        GetData appdata = new GetData(getActivity());
        List<AppUsageInfo> TopApps = appdata.GetTopApps(getActivity());
        for (AppUsageInfo ap : TopApps) {
            entries.add(new PieEntry(ap.getTimeInForegroundPercentage(), ap.getAppName()));
        }
//            entries.add(new PieEntry(80, "颜值"));
//            entries.add(new PieEntry(100, "智慧"));
//            entries.add(new PieEntry(50, "身材"));
//            entries.add(new PieEntry(50, "性格"));
//            entries.add(new PieEntry(20, "声音"));

        //设置数据
        setData(entries);

        //默认动画
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        //设置图例
        Legend l = mPieChart.getLegend();
        //设置图例样式
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(15f);
        l.setTextSize(15f);
        l.setYEntrySpace(15f);
        //设置显示的位置，低版本用的是setPosition();
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        //设置是否显示图例
        l.setDrawInside(false);
        l.setEnabled(true);

        // 输入图例标签样式
        mPieChart.setEntryLabelColor(Color.BLUE);
        mPieChart.setEntryLabelTextSize(18f);

        //设置使用记录布局
        initUsingRecord();
        UsingRecordAdapter adapter = new UsingRecordAdapter(getActivity(), R.layout.app_using_item, lastestappList);
        ListView listView = root.findViewById(R.id.use_record_list);
        listView.setAdapter(adapter);
        return root;
        }

    private void initUsingRecord() {
        GetData appdata = new GetData(getActivity());
        List<AppUsageInfo> LastestApps = appdata.GetLastestApps(getActivity());
        for(AppUsageInfo ap :LastestApps){
            UsingRecord u = new UsingRecord(ap.getAppName());
            u.setAppIcon(ap.getAppIcon());
            u.setAppName(ap.getAppName());
            u.setTime(appdata.LongToString_Time2(ap.getLastRunningTime()));
            lastestappList.add(u);
        }
//        appInfoList =  appInfoProvider.getAllApps();
//        for (int i = 0; i < appInfoList.size(); i++) {
////            if (appInfoList.get(i).getIcon().)
////            Log.d("myDebug", appInfoList.get(i).getAppName());
//        }
    }

    //设置数据
    private void setData (ArrayList < PieEntry > entries) {
        List<Integer> colortables = new ArrayList<>();
        colortables.add(Color.parseColor("#FA8072"));
        colortables.add(Color.parseColor("#fab150"));
        colortables.add(Color.parseColor("#edf54f"));
        colortables.add(Color.parseColor("#a4f578"));
        colortables.add(Color.parseColor("#96dfe6"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        //设置个饼状图之间的距离
        dataSet.setSliceSpace(0f);
        //颜色的集合，按照存放的顺序显示
//            ArrayList<Integer> colors = new ArrayList<Integer>();
//            for (int c : ColorTemplate.VORDIPLOM_COLORS)
//                colors.add(c);
//            for (int c : ColorTemplate.JOYFUL_COLORS)
//                colors.add(c);
//            for (int c : ColorTemplate.COLORFUL_COLORS)
//                colors.add(c);
//            for (int c : ColorTemplate.LIBERTY_COLORS)
//                colors.add(c);
//            for (int c : ColorTemplate.PASTEL_COLORS)
//                colors.add(c);
//            colors.add(ColorTemplate.getHoloBlue());
        List<Integer> colors = new ArrayList<>();
//            colors.add(Color.parseColor("#4A92FC"));
//            colors.add(Color.parseColor("#ee6e55"));
            for (int i = 0; i <entries.size() ; i++) {
                colors.add(colortables.get(i));
            }
            dataSet.setColors(colors);

            //设置折线
            dataSet.setValueLinePart1OffsetPercentage(80.f);
            //设置线的长度
            dataSet.setValueLinePart1Length(0.3f);
            dataSet.setValueLinePart2Length(0.3f);
            //设置文字和数据图外显示
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            //百分比设置
            data.setValueFormatter(new PercentFormatter());
            //文字的颜色
            data.setValueTextSize(14f);
            data.setValueTextColor(Color.BLACK);
            mPieChart.setData(data);
            // 撤销所有的亮点
            mPieChart.highlightValues(null);
            mPieChart.invalidate();
    }
//    private void initFeatures() {
//        Features applicationList = new Features("应用列表", R.drawable.application_list);
//        featuresList.add(applicationList);
//        Features battery = new Features("电池情况", R.drawable.battery);
//        featuresList.add(battery);
//        Features features3 = new Features("features3", R.drawable.features3);
//        featuresList.add(features3);
//        Features features4 = new Features("features4", R.drawable.features4);
//        featuresList.add(features4);
//    }


}