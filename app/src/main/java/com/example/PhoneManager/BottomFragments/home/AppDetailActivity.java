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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.PhoneManager.AppUsageInfo;
import com.example.PhoneManager.BottomFragments.home.util.LineChartMarkView;
import com.example.PhoneManager.GetData;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppDetailActivity extends AppCompatActivity {
    private TextView name;
    private ImageView icon;
    private String apppackagename;
    private String appversion;
    private HashMap<String, AppUsageInfo> AllAppData;

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
        lineChart = (LineChart) findViewById(R.id.lc);
        //获取app所有数据
        GetData G = new GetData(this);
        long starttime = G.getStartTime();
        long endtime = System.currentTimeMillis();
        AllAppData = G.getUsageStatistics(starttime,endtime,this);
        //通过Intent获取app名称、图标
//        Intent intent = getIntent();
//        byte buff[]=intent.getByteArrayExtra("appiconbyte");
//        Bitmap iconbitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
//        name.setText(intent.getStringExtra("appname"));
//        pack.setText(intent.getStringExtra("apppackagename"));
//        ver.setText(intent.getStringExtra("appversion"));
//        icon.setImageBitmap(iconbitmap);
//        apppackagename = intent.getStringExtra("apppackagename");
//        appversion = intent.getStringExtra("appversion");
        apppackagename = "com.tencent.mobileqq";
        initChart(lineChart);
        showLineChart();
        Drawable drawable = getResources().getDrawable(R.drawable.line_color_fade);
        setChartFillDrawable(drawable);
        setMarkerView();
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
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);
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
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 展示曲线
     */
    public void showLineChart() {
//        List<Entry> entries = new ArrayList<>();
//        for (int i = 0; i < dataList.size(); i++) {
//            IncomeBean data = dataList.get(i);
//            /**
//             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
//             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
//             */
//            Entry entry = new Entry(i, (float) data.getValue());
//            entries.add(entry);
//        }
        // 每一个LineDataSet代表一条线
        List<Entry> yVals1 = new ArrayList<>();
        float[] ys1 = new float[] {
                19f, 19f, 18f, 18f, 18f, 18f, 17f, 16f, 17f, 19f,
                21f, 21f, 23f, 23f, 24f, 24f, 25f, 25f, 25f, 24f};
        for (int i = 0; i < ys1.length; i++) {
            yVals1.add(new Entry((i + 1) * 3,ys1[i]));
        }
        // 2.分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet = new LineDataSet(yVals1, "温度");
        //不显示点
        lineDataSet.setDrawCircles(false);
        //不显示值
        lineDataSet.setDrawValues(false);
        initLineDataSet(lineDataSet,R.color.linecolor, LineDataSet.Mode.LINEAR);
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