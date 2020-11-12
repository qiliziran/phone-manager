package com.example.PhoneManager.BottomFragments.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PhoneManager.AppInfo;
import com.example.PhoneManager.AppInfoAdapter;
import com.example.PhoneManager.AppInfoProvider;

import com.example.PhoneManager.AppUsageInfo;
import com.example.PhoneManager.BottomFragments.home.util.SearchItemAdapter;
import com.example.PhoneManager.BottomFragments.home.util.SearchItemBean;
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
import static com.veken.chartview.DensityUtils.dip2px;

public class HomeFragment extends Fragment implements SearchView.SearchViewListener{

    /**
     * 搜索结果列表view
     */
    private ListView lvResults;

    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
//    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private SearchItemAdapter autoCompleteAdapter;


    /**
     * 数据库数据，总数据
     */
    private List<SearchItemBean> dbData;

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<SearchItemBean> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<Bean> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 10;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
        HomeFragment.hintSize = hintSize;
    }



    private Button btn_predict;
    private ListView listView;
    private EditText searchedit;
    private TextView todayusetext,lastestuse;
    private LinearLayout piechart;
    private FrameLayout searchlayout;
    private HomeViewModel homeViewModel;
    private PieChart mPieChart;
    private List<UsingRecord> lastestappList = new ArrayList<>();
    private List<Features> featuresList = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        //今日使用最多app
        mPieChart = (PieChart) root.findViewById(R.id.mPieChart);
        initPieChart();
        //最近使用app，加载慢是因为获取图标！尝试将图标存进全局变量中
//        final ListView listView = root.findViewById(R.id.use_record_list);
        initUsingRecord();
//        UsingRecordAdapter adapter = new UsingRecordAdapter(getActivity(), R.layout.app_using_item, lastestappList);
//        listView.setAdapter(adapter);

        initData();
        initViews(root);
        todayusetext = root.findViewById(R.id.todayusetext);
        lastestuse = root.findViewById(R.id.lastestuse);
        piechart = root.findViewById(R.id.piechart);
        searchlayout = root.findViewById(R.id.searchlayout);
        //设置搜索框内图片大小
        paddingPicture(searchView.getEtInput(),R.drawable.search_icon);
        //取控件当前的布局参数

        //点击搜索框，隐藏下方组件布局,注意这里需要设置触摸和点击两个事件！
        searchView.getEtInput().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayusetext.setVisibility(View.GONE);
                piechart.setVisibility(View.GONE);
//                searchlayout.setla
                mPieChart.setVisibility(View.GONE);
                lastestuse.setVisibility(View.GONE);
                searchView.getTextBack().setVisibility(View.VISIBLE);
                //设置搜索框宽度，漂亮！
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchlayout.getLayoutParams();
                //设置宽度值
                params.width = dip2px(getActivity(), 310);
                //使设置好的布局参数应用到控件
                searchlayout.setLayoutParams(params);
//                listView.setVisibility(View.GONE);
            }
        });
        searchView.getEtInput().setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if(touch_flag==2){
                    //自己业务
                    todayusetext.setVisibility(View.GONE);
                    piechart.setVisibility(View.GONE);
                    mPieChart.setVisibility(View.GONE);
                    lastestuse.setVisibility(View.GONE);
                    searchView.getTextBack().setVisibility(View.VISIBLE);
                    //设置搜索框宽度，漂亮！
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchlayout.getLayoutParams();
                    //设置宽度值
                    params.width = dip2px(getActivity(), 310);
                    //使设置好的布局参数应用到控件
                    searchlayout.setLayoutParams(params);
//                    listView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        searchView.getTextBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.getTextBack().setVisibility(View.GONE);
                todayusetext.setVisibility(View.VISIBLE);
                searchView.getIvDelete().setVisibility(View.GONE);
                searchView.getLvTips().setVisibility(View.GONE);
                piechart.setVisibility(View.VISIBLE);
                mPieChart.setVisibility(View.VISIBLE);
                lastestuse.setVisibility(View.VISIBLE);
                //设置搜索框宽度，漂亮！
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchlayout.getLayoutParams();
                //设置宽度值
                params.width = dip2px(getActivity(), 360);
                //使设置好的布局参数应用到控件
                searchlayout.setLayoutParams(params);
//                listView.setVisibility(View.VISIBLE);
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        return root;
    }

    public void initPieChart(){
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
    }


    /**
     * 初始化ListView列表
      */
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

        List<Integer> colors = new ArrayList<>();
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

    /**
     * 初始化视图
     */
    private void initViews(View v) {
        lvResults = (ListView) v.findViewById(R.id.main_lv_search_results);
        searchView = (SearchView) v.findViewById(R.id.main_search_layout);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //从数据库获取数据
        getDbData();
        //初始化自动补全数据
        getAutoCompleteData(null);
    }

    /**
     * 获取db 数据
     */
    private void getDbData() {
        AppInfoProvider appinfoPro = new AppInfoProvider(getActivity());
        List<AppInfo> appinfo = appinfoPro.getAllApps();
        dbData = new ArrayList<>();
        for (int i = 0; i < appinfo.size(); i++) {
            dbData.add(new SearchItemBean(getBitmapFromDrawable(appinfo.get(i).getIcon()),appinfo.get(i).getAppName(),appinfo.get(i).getPackageName(),appinfo.get(i).getVersionName()));
        }
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).getAppName().contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i));
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new SearchItemAdapter(getActivity(),R.layout.autocomplete_item, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {
        //在自定义view中跳转到另一个页面！漂亮
//        Intent intent = new Intent(getContext(),AppDetailActivity.class);
////                intent.put
//        intent.putExtra("appname",text);
//        intent.putExtra("appicon",icon);
//        getActivity().startActivity(intent);
//        Toast.makeText(getActivity(), "完成搜素", Toast.LENGTH_SHORT).show();
    }

    /**
     * 图片：Drawble-->Bitmap
     * @param drawable
     * @return
     */
    static public Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        // 部分应用没有图标，会返回AdaptiveiconDrawable，用这种方式也能转换为Bitmap
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    /**
     * 设置组件内图片大小
     * @param tv
     * @param pic
     */
    void paddingPicture(EditText tv, int pic) {
        Drawable drawable1 = getResources().getDrawable(pic);
        drawable1.setBounds(0, 0, 55, 55);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tv.setCompoundDrawables(drawable1, null, null, null);//只放左边
    }

}