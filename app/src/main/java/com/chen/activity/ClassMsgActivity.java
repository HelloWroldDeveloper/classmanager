package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.chen.data.User;
import com.chen.handle.DataInput;

//班级信息 activity
public class ClassMsgActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_msg);
        TextView title=(TextView)findViewById(R.id.action_bar_text);
        TextView class_number=(TextView)findViewById(R.id.class_msg_class_number);
        TextView members_num=(TextView)findViewById(R.id.class_msg_people_num);
        Button info=(Button)findViewById(R.id.class_msg_btn);
        ActionBar bar=getSupportActionBar();
        title.setText("班级信息");
        if(bar!=null){
            bar.hide();//隐藏系统默认的标题栏
        }
        class_number.setText(DataInput.getClassNumber());
        members_num.setText(User.getLatestUsers().size()+"");
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //"查看"按钮的点击事件处理
                ClassMembersActivity.actionStart(ClassMsgActivity.this);
            }
        });
    }
    public static void actionStart(Context context){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_MSG");
        context.startActivity(intent);
    }
}
