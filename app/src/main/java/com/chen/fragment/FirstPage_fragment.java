package com.chen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.activity.ClassMsgActivity;
import com.chen.activity.R;
//"首页"碎片
public class FirstPage_fragment extends Fragment {
    private LinearLayout class_msg;//"班级信息"布局框
    private LinearLayout vote;//"投票"布局框
    private LinearLayout disk;//"网盘"布局框
    private LinearLayout wechat;//"公众号"布局框
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.first_page,container,false);
        class_msg=view.findViewById(R.id.class_msg);
        vote=view.findViewById(R.id.vote);
        disk=view.findViewById(R.id.disk);
        wechat=view.findViewById(R.id.wechat);
        addEvent();
        return view;
    }
    private void addEvent(){
        class_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"班级信息"布局框的点击事件处理
                ClassMsgActivity.actionStart(getActivity());
                //在此书写"班级信息"布局框的点击事件处理
            }
        });
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"投票"布局框的点击事件处理

                //在此书写"投票"布局框的点击事件处理
            }
        });
        disk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"网盘"布局框的点击事件处理

                //在此书写"网盘"布局框的点击事件处理
            }
        });
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"公众号"布局框的点击事件处理

                //在此书写"公众号"布局框的点击事件处理
            }
        });
    }
}
