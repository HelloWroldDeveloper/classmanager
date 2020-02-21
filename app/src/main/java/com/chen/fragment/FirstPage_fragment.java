package com.chen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chen.activity.ClassMsgActivity;
import com.chen.activity.R;
import com.chen.activity.VoteListActivity;
import com.chen.handle.ScreenUtil;

//"首页"碎片
public class FirstPage_fragment extends BaseFragment {
    private Button class_msg;//"班级信息"布局框
    private Button vote;//"投票"布局框

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScreenUtil.adapterScreen(getActivity(),480,false);
        View view=inflater.inflate(R.layout.first_page,container,false);
        class_msg=view.findViewById(R.id.class_msg);
        vote=view.findViewById(R.id.vote);
        addEvent();
        return view;
    }
    private void addEvent(){
        class_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"班级信息"按钮的点击事件处理
                ClassMsgActivity.actionStart(getActivity());
                //在此书写"班级信息"按钮的点击事件处理
            }
        });
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"投票"按钮的点击事件处理
                VoteListActivity.actionStart(getActivity());
                //在此书写"投票"按钮的点击事件处理
            }
        });

    }
}
