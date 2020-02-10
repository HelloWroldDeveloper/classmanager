package com.chen.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.chen.activity.R;

//自定义控件(标题栏)
public class ActionbarLayout extends LinearLayout {
    public ActionbarLayout(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.actionbar,this);
        ImageButton btn=findViewById(R.id.action_bar_btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });
    }
}
