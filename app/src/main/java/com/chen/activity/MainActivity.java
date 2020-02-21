package com.chen.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.data.ClassSign;
import com.chen.data.Grade;
import com.chen.data.LeaveApplication;
import com.chen.data.Notice;
import com.chen.data.User;
import com.chen.data.Vote;
import com.chen.fragment.FirstPage_fragment;
import com.chen.fragment.Me_fragment;
import com.chen.fragment.Notice_fragment;
import com.chen.handle.DataInput;
import com.chen.handle.DataOutput;
import com.chen.handle.Util;
import com.yang.inter.AppInterface;

import org.litepal.LitePal;

//主活动
public class MainActivity extends BaseActivity {
    private Button first_page_textView;//"首页"按钮
    private Button notice_textView;//"通知"按钮
    private Button me_textView;//"我的"按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if(!User.getNowUser().getNumber().equals("")){
            setContentView(R.layout.activity_main);
            first_page_textView=(Button) findViewById(R.id.first_page_btn);
            notice_textView=(Button) findViewById(R.id.notice_btn);
            me_textView=(Button) findViewById(R.id.me_btn);
            Util.replaceFragment(this,R.id.main_fragment,new FirstPage_fragment());
            addEvent();
        }else{
            setContentView(R.layout.register);
            final EditText number=findViewById(R.id.register_username);
            final EditText password1=findViewById(R.id.register_password1);
            final EditText password2=findViewById(R.id.register_password2);
            final EditText email=findViewById(R.id.register_email);
            Button register=findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String num=number.getText().toString();
                    String p1=password1.getText().toString();
                    String p2=password2.getText().toString();
                    String e=email.getText().toString();
                    if(num.equals("")){
                        Toast.makeText(MainActivity.this,"请填写学号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(p1.equals("")||p2.equals("")){
                        Toast.makeText(MainActivity.this,"请填写密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(e.equals("")){
                        Toast.makeText(MainActivity.this,"请填写邮箱",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(num.length()!=13){
                        Toast.makeText(MainActivity.this,"学号格式有误",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!p1.equals(p2)){
                        Toast.makeText(MainActivity.this,"两次的密码输入不匹配",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(p1.length()<8){
                        Toast.makeText(MainActivity.this,"密码至少要8位",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if((!e.contains("@"))||(!e.contains("."))){
                        Toast.makeText(MainActivity.this,"邮箱格式有误",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DataOutput.user_register(num,e,p1);
                }
            });
        }
    }
    private void addEvent(){
        first_page_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBtnColor();
                first_page_textView.setTextColor(Color.RED);
                Util.replaceFragment(MainActivity.this,R.id.main_fragment,new FirstPage_fragment());//"首页"碎片
            }
        });
        notice_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBtnColor();
                notice_textView.setTextColor(Color.RED);
                Notice.updateNotices(null,MainActivity.this,false);
                Util.replaceFragment(MainActivity.this,R.id.main_fragment,new Notice_fragment());//"通知"碎片
            }
        });
        me_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBtnColor();
                me_textView.setTextColor(Color.RED);
                Util.replaceFragment(MainActivity.this,R.id.main_fragment,new Me_fragment());//"我的"碎片
            }
        });
    }
    private void resetBtnColor(){
        first_page_textView.setTextColor(Color.BLACK);
        notice_textView.setTextColor(Color.BLACK);
        me_textView.setTextColor(Color.BLACK);
    }
    private void init(){
        //初始化主活动
        LitePal.getDatabase();//初始化数据库
        DataInput.updateNowUser(this);//更新当前用户的信息
        if(!User.getNowUser().getNumber().equals("")){
            Notice.initNotices();//初始化通知
            Grade.initGrades();//初始化成绩上传
            User.initUsers();//初始化班级用户
            LeaveApplication.initApplications();//初始化请假申请
            Vote.initVote();//初始化投票
        }
        AppInterface.AppInit();
    }
}
