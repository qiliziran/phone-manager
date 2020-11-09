package com.example.PhoneManager.BottomFragments.home;

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

public class UsingRecordAdapter extends ArrayAdapter<UsingRecord> {
    private int resourceID;

    public UsingRecordAdapter(Context context, int textViewResourceId, List<UsingRecord> objects) {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        UsingRecord usingRecord = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.appIcon = view.findViewById(R.id.icon);
            viewHolder.appName = view.findViewById(R.id.name);
            viewHolder.Time = view.findViewById(R.id.time);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Bitmap bm = usingRecord.getAppIcon();
        viewHolder.appIcon.setImageBitmap(bm);
        viewHolder.appName.setText(usingRecord.getAppName());
        viewHolder.Time.setText(usingRecord.getTime());
        return view;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView Time;
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