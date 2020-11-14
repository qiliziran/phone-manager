package com.example.PhoneManager.BottomFragments.chart;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.PhoneManager.MyDatabaseHelper;
import com.example.PhoneManager.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {

    private ChartViewModel chartViewModel;
    private ViewPager viewPager;
    private List<Fragment> mFragmentList = new ArrayList<>() ;

    // 当前页编号
    private int currIndex = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chartViewModel =
                ViewModelProviders.of(this).get(ChartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        HistoryRecordFragment fragment1 = new HistoryRecordFragment();
        MainUseFragment fragment2 = new MainUseFragment();
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);

        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        //绑定适配器
        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // tablayout，Tab是TabLayout的内部类，且Tab的构造方法是包访问权限
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        // 绑定，要在viewpager设置完数据后，调用此方法，否则不显示 tabs文本
        tabLayout.setupWithViewPager(viewPager);
        //一定要在上面的setupWithViewPager设置完，再设置tab的文字，否则setupWithViewPager会清空tab标题！
        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        tab0.setText("tab0");
        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        tab1.setText("tab1");
        return root;
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/

                    break;
                case 1:

                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }



}


