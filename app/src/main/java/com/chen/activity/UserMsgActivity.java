package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chen.data.User;
import com.chen.handle.Util;

//用户信息 activity
public class UserMsgActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_msg);
        User now_user=User.getNowUser();//获取当前用户
        TextView user_number=findViewById(R.id.person_msg_user_number);
        TextView user_name=findViewById(R.id.person_msg_user_name);
        CheckBox isAdmin=findViewById(R.id.person_msg_user_type);
        TextView title=findViewById(R.id.action_bar_text);
        Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
        if(now_user.isAdmin()){
            //如果当前用户是管理员
            isAdmin.setChecked(true);
        }
        title.setText("用户信息");
        user_name.setText(now_user.getName());
        user_number.setText(now_user.getNumber());
    }
    public static void actionStart(Context context){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_USER_MSG");
        context.startActivity(intent);
    }
}
