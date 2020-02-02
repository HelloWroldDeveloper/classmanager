package com.chen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.activity.AboutActivity;
import com.chen.activity.ClassSignActivity;
import com.chen.activity.LeaveApplicationActivity;
import com.chen.activity.R;
import com.chen.activity.SubmitGradeActivity;
import com.chen.activity.UserMsgActivity;
import com.chen.data.User;
import com.chen.handle.DataInput;
//"我的"碎片
public class Me_fragment extends Fragment {
    private RelativeLayout relativeLayout;
    private TextView submit_grade;
    private TextView ask_for_leave;
    private TextView class_sign;
    private TextView settings;
    private TextView about;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.me,container,false);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.usermsg);
        submit_grade=(TextView)view.findViewById(R.id.submit_grade);
        ask_for_leave=(TextView)view.findViewById(R.id.holiday);
        class_sign=(TextView)view.findViewById(R.id.class_sign);
        settings=(TextView)view.findViewById(R.id.settings);
        about=(TextView) view.findViewById(R.id.about);
        TextView name=(TextView)view.findViewById(R.id.me_welcome);
        name.setText(User.getNowUser().getName()+"，欢迎您");
        addEvent();
        return view;
    }
    private void addEvent(){
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"用户信息"布局框的点击事件处理
                UserMsgActivity.actionStart(getActivity());
                //在此书写"用户信息"布局框的点击事件处理
            }
        });
        submit_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"上传成绩"标签的点击事件处理
                SubmitGradeActivity.actionStart(getActivity());
                //在此书写"上传成绩"标签的点击事件处理
            }
        });
        ask_for_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"请假"标签的点击事件处理
                LeaveApplicationActivity.actionStart(getActivity());
                //在此书写"请假"标签的点击事件处理
            }
        });
        class_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"签到"标签的点击事件处理
                ClassSignActivity.actionStart(getActivity());
                //在此书写"签到"标签的点击事件处理
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"设置"标签的点击事件处理

                //在此书写"设置"标签的点击事件处理
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"设置"标签的点击事件处理
                AboutActivity.actionStart(getActivity());
                //在此书写"设置"标签的点击事件处理
            }
        });
    }
}
