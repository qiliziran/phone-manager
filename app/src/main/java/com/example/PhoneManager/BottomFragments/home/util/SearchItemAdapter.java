package com.example.PhoneManager.BottomFragments.home.util;

import android.content.Context;
import android.view.View;

import com.example.PhoneManager.BottomFragments.home.UsingRecord;
import com.example.PhoneManager.R;
import com.example.PhoneManager.BottomFragments.home.Bean;
import com.example.PhoneManager.BottomFragments.home.util.CommonAdapter;
import com.example.PhoneManager.BottomFragments.home.util.ViewHolder;

import java.util.List;

/**
 * Created by yetwish on 2015-05-11
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.PhoneManager.AppInfo;
import com.example.PhoneManager.AppInfoAdapter;
import com.example.PhoneManager.R;

import java.util.List;

public class SearchItemAdapter extends ArrayAdapter<SearchItemBean> {
    private int resourceID;

    public SearchItemAdapter(Context context, int textViewResourceId, List<SearchItemBean> objects) {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SearchItemBean searchItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.appIcon = view.findViewById(R.id.search_item_icon);
            viewHolder.appName = view.findViewById(R.id.search_item_name);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Bitmap bm = searchItem.getAppIcon();
        viewHolder.appIcon.setImageBitmap(bm);
        viewHolder.appName.setText(searchItem.getAppName());
        return view;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView appName;
    }

    static public Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        // 部分应用没有图标，会返回AdaptiveiconDrawable，用这种方式也能转换为Bitmap
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
}

