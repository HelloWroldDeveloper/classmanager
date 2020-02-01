package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.SubmitGradeAdapter;
import com.chen.data.Grade;

import java.text.ParseException;
import java.util.List;

//"成绩上传项目列表"的activity
public class SubmitGradeActivity extends BaseActivity {
    private SubmitGradeAdapter adapter;//列表适配器
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar=getSupportActionBar();
        if(bar!=null){
            bar.hide();//隐藏系统默认的标题栏
        }
        List<Grade> grades=null;
        try{
            grades=Grade.getLatestGrades();//获取最新的Grade列表
        }catch (ParseException e){
            e.printStackTrace();
            Toast.makeText(this,"无法解析日期",Toast.LENGTH_SHORT).show();
            Log.e("SubmitGradeActivity", "解析日期失败" );
            finish();
            return;
        }
        if(grades!=null&&grades.size()>0){
            setContentView(R.layout.submit_grade);
            //设置标题栏
            TextView title=findViewById(R.id.action_bar_text);
            title.setText("上传成绩");
            //设置标题栏
            //用recyclerview显示上传成绩项目
            RecyclerView recyclerView=(RecyclerView)findViewById(R.id.submit_grade_recyclerview);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter=new SubmitGradeAdapter(grades,this);
            recyclerView.setAdapter(adapter);
            //用recyclerview显示上传成绩项目
        }else {
            //没有上传成绩项目，就显示"当前没有发布任何成绩上传"
            setContentView(R.layout.no_submit_grade);
            TextView title=findViewById(R.id.action_bar_text);
            title.setText("上传成绩");
        }
    }
    public static void actionStart(Context context){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_GRADE");
        context.startActivity(intent);
    }
    @Override
    protected void onResume() {
        //更新成绩上传项目 列表
        super.onResume();
        try {
            adapter.setGrades(Grade.getLatestGrades());
            adapter.notifyDataSetChanged();
        }catch (ParseException e){
            e.printStackTrace();
            Log.e("SubmitGradeActivity", "无法解析日期" );
            Toast.makeText(this,"无法解析日期",Toast.LENGTH_SHORT).show();
        }
    }
}
