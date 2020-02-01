package com.chen.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chen.data.Grade;
import com.chen.data.LeaveApplication;
import com.chen.data.Notice;
import com.chen.data.User;
import com.chen.fragment.FirstPage_fragment;
import com.chen.fragment.Me_fragment;
import com.chen.fragment.Notice_fragment;
import com.chen.handle.IDGenerator;
import com.yang.inter.AppInterface;

//主活动
public class MainActivity extends BaseActivity {
    private Button first_page_textView;//"首页"按钮
    private Button notice_textView;//"通知"按钮
    private Button me_textView;//"我的"按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        first_page_textView=(Button) findViewById(R.id.first_page_btn);
        notice_textView=(Button) findViewById(R.id.notice_btn);
        me_textView=(Button) findViewById(R.id.me_btn);
        replaceFragment(new FirstPage_fragment());
        addEvent();
    }
    private void replaceFragment(Fragment fragment){
        //把某个碎片添加到屏幕上
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.main_fragment,fragment);
        transaction.commit();
    }
    private void addEvent(){
        first_page_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBtnColor();
                first_page_textView.setTextColor(Color.RED);
                replaceFragment(new FirstPage_fragment());//"首页"碎片
            }
        });
        notice_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBtnColor();
                notice_textView.setTextColor(Color.RED);
                replaceFragment(new Notice_fragment());//"通知"碎片
            }
        });
        me_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBtnColor();
                me_textView.setTextColor(Color.RED);
                replaceFragment(new Me_fragment());//"我的"碎片
            }
        });
    }
    private void resetBtnColor(){
        first_page_textView.setTextColor(Color.BLACK);
        notice_textView.setTextColor(Color.BLACK);
        me_textView.setTextColor(Color.BLACK);
    }
    private void init(){
        IDGenerator.initGenerator();//初始化ID生成器
        Notice.initNotices();//初始化通知
        Grade.initGrades();//初始化成绩上传
        User.initUsers();//初始化班级用户
        LeaveApplication.initApplications();//初始化请假申请
        AppInterface.AppInit();
        //初始化主活动
        //初始化主活动
    }
}
