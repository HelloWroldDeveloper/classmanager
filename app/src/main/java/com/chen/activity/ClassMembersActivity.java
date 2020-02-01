package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.ClassMembersAdapter;
import com.chen.data.MemberStyle;

import java.util.List;

//班级成员列表 activity
public class ClassMembersActivity extends BaseActivity{
    private ClassMembersAdapter adapter;//列表 适配器
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_members);
        ActionBar bar=getSupportActionBar();
        TextView title=(TextView)findViewById(R.id.action_bar_text);
        //用recyclerview展示用户列表
        List<MemberStyle> styles=MemberStyle.generateMemberStyles();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.class_members_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ClassMembersAdapter(styles,this);
        recyclerView.setAdapter(adapter);
        //用recyclerview展示用户列表
        title.setText("班级成员");
        if(bar!=null){
            bar.hide();//隐藏系统默认的标题栏
        }
    }
    public static void actionStart(Context context){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_MEMBERS");
        context.startActivity(intent);
    }
}
