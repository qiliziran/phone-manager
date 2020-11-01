package com.example.PhoneManager.Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.PhoneManager.MainActivity;
import com.example.PhoneManager.R;
import com.example.PhoneManager.SetAppearence;
import com.example.PhoneManager.ui.home.HomeFragment;
import com.example.PhoneManager.utils.StatusBarUtil;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private ImageView visiable;
    private Button loginbtn;
    private boolean isvisiable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        //修改顶部状态栏为白底黑字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(LoginActivity.this, R.color.white);
        }
        //修改输入框中的图标属性
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        paddingPicture(username, R.drawable.user);
        paddingPicture(password, R.drawable.password1);
        paddingPicture(loginbtn, R.drawable.right);
        visiable = findViewById(R.id.visiable);

        setListeners();
    }

    //监听器
    private void setListeners() {
        OnClick onClick = new OnClick();
        visiable.setOnClickListener(onClick);
        loginbtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.visiable:
                    if (!isvisiable) {
                        //密码可见
                        visiable.setImageResource(R.drawable.pass_visiable);
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isvisiable = true;
                    } else {
                        //密码不可见
                        visiable.setImageResource(R.drawable.pass_invisiable);
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        isvisiable = false;
                    }
                    password.postInvalidate();
                    //切换后将EditText光标置于末尾
                    CharSequence charSequence = password.getText();
                    if (charSequence instanceof Spannable) {
                        Spannable spanText = (Spannable) charSequence;
                        Selection.setSelection(spanText, charSequence.length());
                        break;
                    }
                    break;
//                case R.id.loginbtn:
//                    Intent intent = new Intent();
//                    intent.setClass(LoginActivity.this, MainActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
//                    startActivity(intent);
            }
        }
    }
    void paddingPicture(EditText tv, int pic) {
        Drawable drawable1 = getResources().getDrawable(pic);
        drawable1.setBounds(0, 0, 55, 55);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tv.setCompoundDrawables(drawable1, null, null, null);//只放左边
    }
    void paddingPicture(Button tv, int pic) {
        Drawable drawable1 = getResources().getDrawable(pic);
        drawable1.setBounds(-25, 0, 30, 30);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tv.setCompoundDrawables(null, null, drawable1, null);//只放左边
    }
}