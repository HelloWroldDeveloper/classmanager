package com.chen.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

public class TimeNumberPicker extends NumberPicker {
    public TimeNumberPicker(Context context) {
        super(context);
    }

    public TimeNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setNumberPickerView(child);
    }

    public void setNumberPickerView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextColor(Color.BLACK); //字体颜色
            ((EditText) view).setTextSize(20f);//字体大小
        }
    }
}
