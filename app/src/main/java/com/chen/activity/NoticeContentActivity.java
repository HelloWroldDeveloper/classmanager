package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.NoticeContentAdapter;
import com.chen.data.Notice;

//通知项目的内容的activity
public class NoticeContentActivity extends BaseActivity {
    private TextView text;//标题栏的标签
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_content_recyclerview);
        ActionBar bar=getSupportActionBar();
        if(bar!=null){
            bar.hide();//隐藏系统默认的标题栏
        }
        text=(TextView)findViewById(R.id.action_bar_text);
        //通知内容可能很长,因此用recyclerview来显示
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.notice_content_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent=getIntent();
        NoticeContentAdapter adapter=new NoticeContentAdapter(intent.getStringExtra("content"),intent.getStringExtra("msg"));
        recyclerView.setAdapter(adapter);
        //用recyclerview显示通知内容
        text.setText(intent.getStringExtra("title"));//在标题栏显示"当前通知的标题"
    }
    public static void actionStart(Context context, Notice notice){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CHECK_NOTICE");
        intent.putExtra("content",notice.getContent());
        intent.putExtra("msg",notice.getExtraMsg());
        intent.putExtra("title",notice.getTitle());
        context.startActivity(intent);
    }
}
