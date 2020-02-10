package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chen.data.User;
import com.chen.handle.DataInput;
import com.chen.handle.Util;

//班级信息 activity
public class ClassMsgActivity extends BaseActivity{
    private TextView members_num;
    private Button info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_msg);
        TextView title=findViewById(R.id.action_bar_text);
        TextView class_number=findViewById(R.id.class_msg_class_number);
        members_num=findViewById(R.id.class_msg_people_num);
        info=findViewById(R.id.class_msg_btn);
        title.setText("班级信息");
        Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
        class_number.setText(DataInput.getClassNumber());
        info.setEnabled(false);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //"查看"按钮的点击事件处理
                ClassMembersActivity.actionStart(ClassMsgActivity.this);
            }
        });
        User.updateUsers(members_num,this,info);
    }
    @Override
    protected void onResume() {
        super.onResume();
        User.updateUsers(members_num,this,info);
    }
    public static void actionStart(Context context){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_MSG");
        context.startActivity(intent);
    }
}
