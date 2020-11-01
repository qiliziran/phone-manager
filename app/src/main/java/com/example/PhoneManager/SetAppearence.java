package com.example.PhoneManager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.EditText;

public class SetAppearence {
    Context context;

    public SetAppearence(Context context) {
        this.context = context;
    }
    public void Setdrawableleft(EditText e){
        Drawable drawable= context.getResources().getDrawable(R.drawable.user);
        drawable.setBounds(0,0,35,35);//第一0是距左边距离，第二0是距上边距离，30、35分别是长宽
        e.setCompoundDrawables(drawable,null,null,null);//只放左边
    }
}
