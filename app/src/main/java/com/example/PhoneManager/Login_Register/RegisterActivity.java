package com.example.PhoneManager.Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.PhoneManager.R;
import com.example.PhoneManager.utils.StatusBarUtil;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, phone,password,repassword;
    private ImageView visiable,revisiable;
    private TextView login;
    private Button registerbtn;
    private boolean isvisiable = false;
    private boolean reisvisiable = false;
    private boolean confirmpassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //修改顶部状态栏为白底黑字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(RegisterActivity.this, R.color.white);
        }
        login = findViewById(R.id.login);
        //修改输入框中的图标属性
        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        registerbtn = findViewById(R.id.registerbtn);
        paddingPicture(username, R.drawable.user);
        paddingPicture(phone, R.drawable.phone);
        paddingPicture(password, R.drawable.password1);
        paddingPicture(repassword, R.drawable.repassword);
        paddingPicture(registerbtn, R.drawable.right);
        visiable = findViewById(R.id.visiable);
        revisiable = findViewById(R.id.revisiable);

        setListeners();



    }

    //监听器
    private void setListeners() {
        OnClick onClick = new OnClick();
        visiable.setOnClickListener(onClick);
        revisiable.setOnClickListener(onClick);
        registerbtn.setOnClickListener(onClick);
        login.setOnClickListener(onClick);
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
                case R.id.revisiable:
                    if (!reisvisiable) {
                        //密码可见
                        revisiable.setImageResource(R.drawable.repass_visiable);
                        repassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        reisvisiable = true;
                    } else {
                        //密码不可见
                        revisiable.setImageResource(R.drawable.repass_invisiable);
                        repassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        reisvisiable = false;
                    }
                    repassword.postInvalidate();
                    //切换后将EditText光标置于末尾
                    CharSequence charSequence1 = repassword.getText();
                    if (charSequence1 instanceof Spannable) {
                        Spannable spanText = (Spannable) charSequence1;
                        Selection.setSelection(spanText, charSequence1.length());
                        break;
                    }
                    break;
                case R.id.login:
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, LoginActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                    startActivity(intent);
                    break;
                case R.id.registerbtn:
                    //保存用户名信息，这段代码千万不能放进oncreate函数中否则会 进入死循环，页面一片空白导致卡死
                    String usernametext = username.getText().toString();
                    String phonetext = phone.getText().toString();
                    SharedPreferences.Editor editer = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                    editer.putString("username", usernametext);
                    editer.putString("phonenumber", phonetext);
                    editer.putBoolean("loginstate", false);
//                    while (!confirmpassword){
                    String passwordtext = password.getText().toString();
                    String repasswordtext = repassword.getText().toString();
                    Log.d("TAG", "password: " + passwordtext);
                    Log.d("TAG", "repassword: " + repasswordtext);
                    //比较两个字符串是否相等，用equal！！！
                    if (passwordtext.equals(repasswordtext)) {
                        Log.d("TAG", "onClick: 密码确认成功！");
                        editer.putString("password", passwordtext);
                        editer.apply();
                        Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                        confirmpassword = true;
                    } else {
                        Toast.makeText(RegisterActivity.this, "确认密码错误", Toast.LENGTH_SHORT).show();
                        repassword.setText("");
                    }
//            }

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