package com.example.PhoneManager.BottomFragments.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.PhoneManager.AppUsageInfo;
import com.example.PhoneManager.BottomFragments.home.util.LineChartMarkView;
import com.example.PhoneManager.GetData;
import com.example.PhoneManager.MainActivity;
import com.example.PhoneManager.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppDetailActivity extends AppCompatActivity {
    private TextView name,lastest,sorttext,runtime,launchcount,savedata;
    private ImageView icon,appdetailback;
    private String apppackagename=null;
    private String appversion=null;
    private HashMap<String, AppUsageInfo> AllAppData;
    //运行时间的精度，默认为每分钟
    private final float mindivide = 60*1000;
    private boolean select = true;
    private String ylabel="分钟";
    //线条的渐变背景样式
    private Drawable Linedrawble;
    //线条顶部的颜色
    private String Linecolor;
    //数组的长度
    private int linedatanum;
    private LineChart lineChart;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    //  private MyMarkerView markerView;    //标记视图 即点击xy轴交点时弹出展示信息的View 需自定义

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        name = findViewById(R.id.appname);
        icon = findViewById(R.id.appicon);
        appdetailback = findViewById(R.id.appdetailbackhome);
        lastest = findViewById(R.id.lastestruntime);
        runtime = findViewById(R.id.runtime);
        launchcount = findViewById(R.id.launchcount);
        sorttext = findViewById(R.id.sorttext);
        savedata = findViewById(R.id.savedata);
        lineChart = (LineChart) findViewById(R.id.lc);
        //获取数据
        GetData G = new GetData(this);
        final long starttime = G.getStartTime();
        long endtime = System.currentTimeMillis();
        AllAppData = G.getUsageStatistics(starttime,endtime,this);
        //通过Intent获取app名称、图标
        Intent intent = getIntent();
        byte buff[]=intent.getByteArrayExtra("appiconbyte");
        Bitmap iconbitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
        name.setText(intent.getStringExtra("appname"));
        icon.setImageBitmap(iconbitmap);

        System.out.println(intent.getStringExtra("apppackagename"));
        System.out.println("app名称："+intent.getStringExtra("appname"));
//        apppackagename = intent.getStringExtra("apppackagename").toString();
        savedata.setText(intent.getStringExtra("apppackagename").toString());
        apppackagename = savedata.getText().toString();
        savedata.setText(intent.getStringExtra("appversion"));
        appversion = savedata.getText().toString();
        settext();
        initChart(lineChart);
        Linedrawble = getResources().getDrawable(R.drawable.line_color_fade_time);
        Linecolor = getResources().getString(R.string.linecolor_time);
        startlinechart(lineChart,select,ylabel,Linecolor,Linedrawble);
        appdetailback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent(AppDetailActivity.this, MainActivity.class);
                startActivity(backintent);
            }
        });
        sorttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select){
                    sorttext.setText(R.string.launchcount);
                    ylabel = "次数";
                    select = false;
                    Linedrawble = getResources().getDrawable(R.drawable.line_color_fade_count);
                    Linecolor = getResources().getString(R.string.linecolor_count);
                    startlinechart(lineChart,select,ylabel,Linecolor,Linedrawble);
                }else{
                    sorttext.setText(R.string.runtime);
                    ylabel = "分钟";
                    select =true;
                    Linedrawble = getResources().getDrawable(R.drawable.line_color_fade_time);
                    Linecolor = getResources().getString(R.string.linecolor_time);
                    startlinechart(lineChart,select,ylabel,Linecolor,Linedrawble);
                }
            }
        });

    }

    /**
     * 绘制曲线图的完整流程
     * @param Lc
     * @param Select
     * @param Ylabel
     */
    public void startlinechart(LineChart Lc,boolean Select,String Ylabel,String color,Drawable drawble){
        initChart(Lc);
        showLineChart(Select,Ylabel,color);
        setChartFillDrawable(drawble);
        setMarkerView();
    }

    public void settext(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        System.out.println("传过来的包名:"+apppackagename);
        System.out.println("传过来的版本名:"+appversion);
        for (Map.Entry<String, AppUsageInfo> entry : AllAppData.entrySet()) {
            System.out.println("包名："+entry.getKey()+",");
        }
        if(AllAppData.containsKey(apppackagename)){
            System.out.println("包含！！！");
            AppUsageInfo appinfo= AllAppData.get(apppackagename);
            lastest.setText("最近一次启动："+format.format(new Date(AllAppData.get(apppackagename).getLastRunningTime())));
            System.out.println("微信总运行时间："+appinfo.getTimeInForeground());
            int time =(int)appinfo.getTimeInForeground()/1000;
            System.out.println("微信总运行秒数："+time);
            if(time/60<1){
                runtime.setText(time+"秒");
            }
            else if(time/3600<1){
                int second = time%60;
                int minute = time/60;
                runtime.setText(minute+"分"+second+"秒");
            }else{
                int second = time%60;
                int minute = (time-second)%3600;
                int hour = time/3600;
                runtime.setText(hour+"时"+minute+"分"+second+"秒");
            }

            launchcount.setText(appinfo.getLaunchCount()+"次");
        }
        else{
            System.out.println("不包含？？？。。。");
            lastest.setText("今日未启动");

            runtime.setText("0");
            launchcount.setText("0");
        }
    }

    /**
     * 初始化图表
     */
    private void initChart(LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //设置白色背景
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(1500);
//        lineChart.animateX(1500);
        //去除Description
        lineChart.setDescription(null);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);
        //消除XY轴自身的网格线
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(false);
        //设置X Y轴网格线为虚线（实体线长度、间隔距离、偏移量：通常使用 0）
//        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        //去掉右侧Y轴
        rightYaxis.setEnabled(false);
        xAxis.setDrawAxisLine(false); // 不绘制X轴
        leftYAxis.setDrawAxisLine(false); // 不绘制Y轴
//        leftYAxis.setEnabled(false);
//        xAxis.setEnabled(false);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(15f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
        //设置标签
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == 0 ? "" : value == linedatanum ? "(时)" : value < 10 ? "" + (int) value : (int) value + "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param mode 线条折曲度
     */
    private void initLineDataSet(LineDataSet lineDataSet, LineDataSet.Mode mode) {
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setMode(mode);
        //线宽度
        lineDataSet.setLineWidth(1.6f);

    }

    /**
     * 展示曲线
     */
    public void showLineChart(boolean selectvalue,String ylabel,String color) {
        List<Entry> ydata = new ArrayList<>();
        if(AllAppData.containsKey(apppackagename)){
            int i=0;
            if(selectvalue){
                while (AllAppData.get(apppackagename).getEachHourRunningTimes()[i]!=-1){

                    ydata.add(new Entry(i,(AllAppData.get(apppackagename).getEachHourRunningTimes()[i]/mindivide)));
                    Log.d("微信的使用时长", ydata.get(i).getX()+"点："+AllAppData.get(apppackagename).getEachHourRunningTimes()[i]);
                    i++;
                }
            }else{
                while (AllAppData.get(apppackagename).getEachHourLaunchCounts()[i]!=-1){
                    ydata.add(new Entry(i,(AllAppData.get(apppackagename).getEachHourLaunchCounts()[i])));
                    Log.d("微信的使用时长", ydata.get(i).getX()+"点："+AllAppData.get(apppackagename).getEachHourLaunchCounts()[i]);
                    i++;
                }
            }
            linedatanum=i;
        }else{
            GetData gg = new GetData(this);
            int nowhour = gg.NowHour();
            for (int i = 0; i <=nowhour+1 ; i++) {
                ydata.add(new Entry(i,0));
            }
        }

        // 2.分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet = new LineDataSet(ydata, ylabel);
        //不显示点
        lineDataSet.setDrawCircles(false);
        //不显示值
        lineDataSet.setDrawValues(false);
        initLineDataSet(lineDataSet,LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(Color.parseColor(color));
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView() {
        LineChartMarkView mv = new LineChartMarkView(this, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }

}