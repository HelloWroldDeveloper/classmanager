package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.LeaveApplyContentAdapter;
import com.chen.data.LeaveApplication;

//请假申请内容 activity
public class LeaveApplyContentActivity extends BaseActivity{
    public static final String TAG="请假申请内容(活动)";
    private LeaveApplyContentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_apply_content_recyclerview);
        TextView title=(TextView)findViewById(R.id.action_bar_text);
        int id=getIntent().getIntExtra("id",-1);
        LeaveApplication application=null;
        if(id!=-1){
            application=LeaveApplication.findApplicationByID(id);
        }else{
            Toast.makeText(this,"无法通过id找到对应的请假申请(id参数无法传递)",Toast.LENGTH_LONG);
            Log.e(TAG,"无法通过id找到对应的请假申请(id参数无法传递)");
            finish();
            return;
        }
        if(application!=null){
            title.setText(application.getTitle());//设置请假申请的标题
            ActionBar bar=getSupportActionBar();
            if(bar!=null){
                bar.hide();//隐藏系统默认的标题栏
            }
            //请假申请内容可能很长,因此用recyclerview来显示
            RecyclerView recyclerView=(RecyclerView)findViewById(R.id.leave_apply_content_recyclerView);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter=new LeaveApplyContentAdapter(this,application);
            recyclerView.setAdapter(adapter);
            //用recyclerview显示请假申请内容
        }else{
            Toast.makeText(this,"无法通过id找到对应的请假申请(找不到)",Toast.LENGTH_LONG);
            Log.e(TAG,"无法通过id找到对应的请假申请(找不到)");
            finish();
        }
    }
    public static void actionStart(Context context,LeaveApplication application){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_LEAVE_APPLICATION_CONTENT" );
        intent.putExtra("id",application.getId());
        context.startActivity(intent);
    }
}
