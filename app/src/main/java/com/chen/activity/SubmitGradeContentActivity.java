package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.data.Grade;
import com.chen.handle.Util;

//上传成绩项目的详情 activity
public class SubmitGradeContentActivity extends BaseActivity {
    private static final String TAG="SubmitGradeContent";//用于发日志的tag

    private EditText grade;//"成绩"文本框
    private Button submit;//"上传成绩"按钮
    private TextView tip;//"请输入您的成绩"或"在此修改您的成绩"标签
    private Grade score;//当前活动对应的成绩上传项目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_grade_content);
        Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
        TextView title=findViewById(R.id.action_bar_text);
        title.setText("成绩上传");
        TextView test_name=findViewById(R.id.submit_grade_content_test_name);
        TextView monitor=findViewById(R.id.submit_grade_content_monitor);
        grade=findViewById(R.id.submit_grade_content_score_text);
        submit=findViewById(R.id.submit_grade_ok);
        tip=findViewById(R.id.submit_grade_tip_text);
        int grade_id=Integer.parseInt(getIntent().getStringExtra("id"));
        score=Grade.findGradeByID(grade_id);//通过id找到对应的成绩上传项目
        if(score!=null){
            test_name.setText(score.getTest_name());//获得考试名称
            monitor.setText(score.getMonitor());//获得统计人
            init();
            addEvent();
        }else {
            Log.e(TAG, "无法找到对应id的成绩" );
            Toast.makeText(this,"无法找到对应id的成绩",Toast.LENGTH_SHORT).show();
        }
    }
    private void init(){
        if(score.getNowScore()!=-1){
            //如果用户之前提交过成绩了
            int s=score.getNowScore();//取出之前填写的成绩
            if(score.getStatus()!=Grade.OVER_DUE){
                tip.setText("在此修改您的成绩：");
            }else{
                tip.setText("您的成绩：");
                grade.setEnabled(false);
            }
            this.grade.setText(""+s);
        }
    }
    private void addEvent(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String score=grade.getText().toString();
                try{
                    if(SubmitGradeContentActivity.this.score.is_overdue()){
                        //如果当前成绩上传项目已过期
                        Toast.makeText(SubmitGradeContentActivity.this,"成绩上传已过期",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (java.text.ParseException e){
                    Toast.makeText(SubmitGradeContentActivity.this,"无法解析日期",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e(TAG, "无法解析日期");
                    finish();
                    return;
                }
                if(score.equals("")){
                    Toast.makeText(SubmitGradeContentActivity.this,"请输入成绩",Toast.LENGTH_SHORT).show();
                }else{
                    int value=Integer.parseInt(score);
                    if(value<0){
                        Toast.makeText(SubmitGradeContentActivity.this,"您输入的成绩不合法",Toast.LENGTH_SHORT).show();
                    }else{
                        Grade g=SubmitGradeContentActivity.this.score;
                        g.setNowScore(value);
                        Grade.submit_grade(g,SubmitGradeContentActivity.this);
                    }
                }
            }
        });
    }
    public static void actionStart(Context context, Grade grade){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_SUBMIT_GRADE");
        intent.putExtra("id",grade.getGrade_id()+"");
        context.startActivity(intent);
    }
}
