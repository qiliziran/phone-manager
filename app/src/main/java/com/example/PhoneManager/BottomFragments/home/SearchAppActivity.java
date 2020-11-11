package com.example.PhoneManager.BottomFragments.home;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.PhoneManager.AppInfo;
import com.example.PhoneManager.AppInfoProvider;
import com.example.PhoneManager.BottomFragments.home.util.SearchItemAdapter;
import com.example.PhoneManager.BottomFragments.home.util.SearchItemBean;
import com.example.PhoneManager.R;

import java.util.ArrayList;
import java.util.List;


public class SearchAppActivity extends Activity implements SearchView.SearchViewListener {

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
    private static int DEFAULT_HINT_SIZE = 4;

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
        SearchAppActivity.hintSize = hintSize;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_app);
        initData();
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
        searchView = (SearchView) findViewById(R.id.main_search_layout);
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
        AppInfoProvider appinfoPro = new AppInfoProvider(this);
        List<AppInfo> appinfo = appinfoPro.getAllApps();
        dbData = new ArrayList<>();
        for (int i = 0; i < appinfo.size(); i++) {
            dbData.add(new SearchItemBean(getBitmapFromDrawable(appinfo.get(i).getIcon()),appinfo.get(i).getAppName()));
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
            autoCompleteAdapter = new SearchItemAdapter(this,R.layout.autocomplete_item, autoCompleteData);
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
        Toast.makeText(this, "完成搜素", Toast.LENGTH_SHORT).show();
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

}