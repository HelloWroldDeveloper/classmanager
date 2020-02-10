package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.SubmitGradeAdapter;
import com.chen.data.Grade;
import com.chen.handle.DataOutput;
import com.chen.handle.Util;

import java.util.List;

//"成绩上传项目列表"的activity
public class SubmitGradeActivity extends BaseActivity {
    private SubmitGradeAdapter adapter;//列表适配器
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
        List<Grade> grades;
        if(Grade.isAllow_activity_update()){
            Grade.updateGrades(this,null);//更新Grade列表
            Grade.setAllow_activity_update(false);
        }
        grades=Grade.getGrades();
        if(grades!=null&&grades.size()>0){
            setContentView(R.layout.submit_grade);
            //设置标题栏
            TextView title=findViewById(R.id.action_bar_text);
            title.setText("上传成绩");
            //设置标题栏
            //用recyclerView显示上传成绩项目
            RecyclerView recyclerView=findViewById(R.id.submit_grade_recyclerview);
            adapter=new SubmitGradeAdapter(grades,this);
            Util.loadRecyclerView(recyclerView,new LinearLayoutManager(this),adapter);
            //用recyclerView显示上传成绩项目
        }else {
            //没有上传成绩项目，就显示"当前没有发布任何成绩上传"
            setContentView(R.layout.no_submit_grade);
            TextView title=findViewById(R.id.action_bar_text);
            title.setText("上传成绩");
        }
    }
    public static void actionStart(Context context){
        //调用该方法以启动当前活动
        Grade.setAllow_activity_update(true);
        Intent intent=new Intent("com.chen.MainActivity.ACTION_GRADE");
        context.startActivity(intent);
    }
    @Override
    protected void onResume() {
        //更新成绩上传项目 列表
        super.onResume();
        Grade.updateGrades(this,adapter);//获取最新的Grade列表
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataOutput.saveGrades();
    }
}
