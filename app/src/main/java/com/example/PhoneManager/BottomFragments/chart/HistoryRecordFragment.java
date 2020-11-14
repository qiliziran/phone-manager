package com.example.PhoneManager.BottomFragments.chart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.PhoneManager.ApplicationListActivity;
import com.example.PhoneManager.BottomFragments.me.MeFragment;
import com.example.PhoneManager.Login_Register.LoginActivity;
import com.example.PhoneManager.R;

import static android.content.Context.MODE_PRIVATE;

public class HistoryRecordFragment extends Fragment {
    private TextView selectmode,starttime,endtime;
    private View timeline;
    private boolean isSelect = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historyrecord, container, false) ;
        selectmode =  root.findViewById(R.id.selectmode);
        starttime =  root.findViewById(R.id.starttime);
        timeline = root.findViewById(R.id.timeline);
        endtime =  root.findViewById(R.id.endtime);

        setListeners();
        return root;
    }

    //监听器
    private void setListeners() {
        OnClick onClick = new OnClick();
        selectmode.setOnClickListener(onClick);
        starttime.setOnClickListener(onClick);
        timeline.setOnClickListener(onClick);
        endtime.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.selectmode:
                    if(isSelect){
                        final CommonDialog dialog = new CommonDialog(getContext());
                        setDialogPosition(dialog);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        dialog.setUpBackground(R.drawable.background_mode_up)
                              .setUpTextColor(getResources().getColor(R.color.white))
                                .setDownBackground(R.drawable.background_mode_down_normal)
                                .setDownTextColor(R.color.grey)
                                .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        selectmode.setText("近一小时");
                                        starttime.setVisibility(View.INVISIBLE);
                                        timeline.setVisibility(View.INVISIBLE);
                                        endtime.setVisibility(View.INVISIBLE);

                                        isSelect = true;
                                        dialog.setUpBackground(R.drawable.background_mode_up)
                                                .setUpTextColor(getResources().getColor(R.color.white))
                                                .setDownBackground(R.drawable.background_mode_down_normal)
                                                .setDownTextColor(R.color.grey);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onNegtiveClick() {
                                        selectmode.setText("自定义");
                                        starttime.setVisibility(View.VISIBLE);
                                        timeline.setVisibility(View.VISIBLE);
                                        endtime.setVisibility(View.VISIBLE);
                                        isSelect = false;
                                        dialog.setDownBackground(R.drawable.background_mode_down)
                                                .setDownTextColor(getResources().getColor(R.color.white))
                                                .setUpBackground(R.drawable.background_mode_up_normal)
                                                .setUpTextColor(R.color.grey);
                                        dialog.dismiss();
                                    }
                                }).show();
                    }else{
                        final CommonDialog dialog = new CommonDialog(getContext());
                        setDialogPosition(dialog);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        dialog.setDownBackground(R.drawable.background_mode_down)
                                .setDownTextColor(getResources().getColor(R.color.white))
                                .setUpBackground(R.drawable.background_mode_up_normal)
                                .setUpTextColor(R.color.grey)
                                .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        selectmode.setText("近一小时");
                                        starttime.setVisibility(View.INVISIBLE);
                                        timeline.setVisibility(View.INVISIBLE);
                                        endtime.setVisibility(View.INVISIBLE);
                                        isSelect = true;
                                        dialog.setUpBackground(R.drawable.background_mode_up)
                                                .setUpTextColor(getResources().getColor(R.color.white))
                                                .setDownBackground(R.drawable.background_mode_down_normal)
                                                .setDownTextColor(R.color.grey);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onNegtiveClick() {
                                        selectmode.setText("自定义");
                                        starttime.setVisibility(View.VISIBLE);
                                        timeline.setVisibility(View.VISIBLE);
                                        endtime.setVisibility(View.VISIBLE);
                                        isSelect = false;
                                        dialog.setDownBackground(R.drawable.background_mode_down)
                                                .setDownTextColor(getResources().getColor(R.color.white))
                                                .setUpBackground(R.drawable.background_mode_up_normal)
                                                .setUpTextColor(R.color.grey);
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                    break;
                case R.id.starttime:
                    break;
                case R.id.endtime:

                    break;
            }
        }
    }

    /**
     * 设置弹框的位置
     * @param dialog
     */
    void setDialogPosition(CommonDialog dialog){
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
        lp.x = 100; // 新位置X坐标
        lp.y = 500; // 新位置Y坐标
        lp.width = 300; // 宽度
        lp.height = 300; // 高度
        lp.alpha = 0.9f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
    }

}

