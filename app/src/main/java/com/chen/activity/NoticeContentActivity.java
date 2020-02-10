package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.NoticeContentAdapter;
import com.chen.data.Notice;
import com.chen.handle.Util;

//通知项目的内容的activity
public class NoticeContentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_content_recyclerview);
        Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
        TextView text=findViewById(R.id.action_bar_text);
        Intent intent=getIntent();
        //通知内容可能很长,因此用recyclerView来显示
        RecyclerView recyclerView=findViewById(R.id.notice_content_recyclerView);
        NoticeContentAdapter adapter=new NoticeContentAdapter(intent.getStringExtra("content"),intent.getStringExtra("msg"));
        Util.loadRecyclerView(recyclerView,new LinearLayoutManager(this),adapter);
        //用recyclerView显示通知内容
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
