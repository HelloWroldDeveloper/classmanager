package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.LeaveApplyadapter;
import com.chen.data.LeaveApplication;
import com.chen.handle.DataOutput;
import com.chen.handle.Util;

import java.util.List;

//请假申请列表 activity
public class LeaveApplicationActivity extends BaseActivity {
    private LeaveApplyadapter adapter;
    private ImageButton add_application;
    private ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取最新的请假申请列表
        LeaveApplication.updateApplications();
        List<LeaveApplication> applications=LeaveApplication.getApplications();
        //获取最新的请假申请列表
        if(applications.size()==0){
            //如果没有请假申请
            setContentView(R.layout.no_leave);
        }else{
            setContentView(R.layout.leave_list);
            //用recyclerView显示请假申请列表
            RecyclerView recyclerView=findViewById(R.id.leave_recyclerView);
            adapter=new LeaveApplyadapter(applications,this);
            Util.loadRecyclerView(recyclerView,new LinearLayoutManager(this),adapter);
            //用recyclerView显示请假申请列表
        }
        back=findViewById(R.id.action_bar_btn);
        add_application=findViewById(R.id.leave_add_apply);
        Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
        addEvent();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //更新请假申请列表
        //获取最新的请假申请列表
        LeaveApplication.updateApplications();
        List<LeaveApplication> applications=LeaveApplication.getApplications();
        //获取最新的请假申请列表
        adapter.setApplications(applications);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataOutput.saveApplication();
    }

    private void addEvent(){
        add_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //"添加"按钮的点击事件处理
                LeaveApplyAddActivity.actionStart(LeaveApplicationActivity.this);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //"返回"按钮的点击事件处理
                LeaveApplicationActivity.this.finish();
            }
        });
    }
    public static void actionStart(Context context){
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_LEAVE_APPLICATION");
        context.startActivity(intent);
    }
}
