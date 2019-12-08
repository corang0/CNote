package com.superdroid.cnote;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog {
    private TextView color0;
    private TextView color1;
    private TextView color2;
    private TextView color3;
    private TextView color4;
    private TextView color5;
    private TextView color6;
    private TextView color7;
    private TextView color8;

    private View.OnClickListener mColorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.custom_dialog);

        color0 = (TextView) findViewById(R.id.color0);
        color1 = (TextView) findViewById(R.id.color1);
        color2 = (TextView) findViewById(R.id.color2);
        color3 = (TextView) findViewById(R.id.color3);
        color4 = (TextView) findViewById(R.id.color4);
        color5 = (TextView) findViewById(R.id.color5);
        color6 = (TextView) findViewById(R.id.color6);
        color7 = (TextView) findViewById(R.id.color7);
        color8 = (TextView) findViewById(R.id.color8);

        color0.setOnClickListener(mColorListener);
        color1.setOnClickListener(mColorListener);
        color2.setOnClickListener(mColorListener);
        color3.setOnClickListener(mColorListener);
        color4.setOnClickListener(mColorListener);
        color5.setOnClickListener(mColorListener);
        color6.setOnClickListener(mColorListener);
        color7.setOnClickListener(mColorListener);
        color8.setOnClickListener(mColorListener);
    }

    //생성자 생성
    public CustomDialog(@NonNull Context context, View.OnClickListener colorListener) {
        super(context);
        this.mColorListener = colorListener;
    }
}
