package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chen.data.LeaveApplication;
import com.chen.data.MyDate;
import com.chen.fragment.Leave_apply_add_s1_fragment;
import com.chen.fragment.Leave_apply_add_s3_fragment;
import com.chen.handle.DataOutput;

import java.text.ParseException;

public class LeaveApplyAddActivity extends BaseActivity{
    private static final String TAG="ADD_APPLY";

    private Button quit;
    private Button back;
    private Button next;
    private int now_step=1;
    private Leave_apply_add_s1_fragment s1_fragment;
    private Leave_apply_add_s3_fragment s3_fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_apply_add);
        quit=findViewById(R.id.leave_apply_add_quit);
        back=findViewById(R.id.leave_apply_add_back);
        next=findViewById(R.id.leave_apply_add_next);
        s1_fragment=new Leave_apply_add_s1_fragment();
        back.setEnabled(false);
        replaceFragment(s1_fragment);
        addEvent();
    }

    private void addEvent(){
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveApplyAddActivity.this.finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //"上一步"按钮的点击事件处理
                if(now_step==3){
                    back.setBackgroundResource(R.drawable.btn_grey);
                    back.setEnabled(false);
                    next.setEnabled(true);
                    next.setText("下一步");
                    s1_fragment=new Leave_apply_add_s1_fragment();
                    replaceFragment(s1_fragment);
                    now_step=1;
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //"下一步"或"完成"按钮的点击事件处理
                if(now_step==1){
                    //检查用户是否正确填写
                    if(s1_fragment.getTitle().equals("")){
                        Toast.makeText(LeaveApplyAddActivity.this,"请填写申请标题",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(s1_fragment.getContent().equals("")){
                        Toast.makeText(LeaveApplyAddActivity.this,"请填写申请正文",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //检查用户是否正确填写
                    back.setBackgroundResource(R.drawable.btn_yellow);
                    back.setEnabled(true);
                    next.setText("完成");
                    s3_fragment=new Leave_apply_add_s3_fragment();
                    replaceFragment(s3_fragment);
                    now_step=3;
                }else if(now_step==3){
                    //检查用户是否正确填写
                    int[] start_d=s3_fragment.getStart_date();
                    int[] end_d=s3_fragment.getEnd_date();
                    int[] start_t=s3_fragment.getStart_time();
                    int[] end_t=s3_fragment.getEnd_time();
                    if(start_d==null){
                        Toast.makeText(LeaveApplyAddActivity.this,"请选择开始日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(start_t==null){
                        Toast.makeText(LeaveApplyAddActivity.this,"请选择开始时间",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(end_d==null){
                        Toast.makeText(LeaveApplyAddActivity.this,"请选择结束日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(end_t==null){
                        Toast.makeText(LeaveApplyAddActivity.this,"请选择结束时间",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MyDate start=new MyDate(start_d[0],start_d[1],start_d[2],start_t[0],start_t[1]),
                            end=new MyDate(end_d[0],end_d[1],end_d[2],end_t[0],end_t[1]);
                    try{
                        if(MyDate.before_now(start)){
                            Toast.makeText(LeaveApplyAddActivity.this,"错误:开始日期比当前日期早",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(MyDate.before_now(end)){
                            Toast.makeText(LeaveApplyAddActivity.this,"错误:结束日期比当前日期早",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (end.before(start)){
                            Toast.makeText(LeaveApplyAddActivity.this,"错误:结束日期比开始日期早",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (ParseException e){
                        Log.e(TAG, "无法解析日期" );
                        Toast.makeText(LeaveApplyAddActivity.this,"无法解析日期",Toast.LENGTH_SHORT);
                        LeaveApplyAddActivity.this.finish();
                    }
                    //检查用户是否正确填写
                    LeaveApplication application=new LeaveApplication(s1_fragment.getTitle(),s1_fragment.getContent()
                    ,start,end);
                    LeaveApplication.addNewApplication(application);
                    Toast.makeText(LeaveApplyAddActivity.this,"成功创建申请",Toast.LENGTH_SHORT).show();
                    s3_fragment.setCreating(true);
                    DataOutput.deleteApplication(LeaveApplyAddActivity.this);
                    LeaveApplyAddActivity.this.finish();
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        //把某个碎片添加到屏幕上
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.leave_apply_add_fragment,fragment);
        transaction.commit();
    }
    public static void actionStart(Context context){
        //执行该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_ADD_LEAVE_APPLICATION");
        context.startActivity(intent);
    }
}
