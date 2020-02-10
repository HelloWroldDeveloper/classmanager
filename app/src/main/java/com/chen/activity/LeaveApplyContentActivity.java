package com.chen.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.adapter.LeaveApplyContentAdapter;
import com.chen.data.LeaveApplication;
import com.chen.handle.Util;

//请假申请内容 activity
public class LeaveApplyContentActivity extends BaseActivity{
    public static final String TAG="请假申请内容(活动)";
    private LeaveApplication application;//当前的请假申请

    private ImageButton back;
    private Button delete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_apply_content_recyclerview);
        TextView title=findViewById(R.id.action_bar_text);
        back=findViewById(R.id.action_bar_btn);
        delete=findViewById(R.id.leave_apply_delete);
        int id=getIntent().getIntExtra("id",-1);
        application=null;
        if(id!=-1){
            application=LeaveApplication.findApplicationByID(id);
        }else{
            Toast.makeText(this,"无法通过id找到对应的请假申请(id参数无法传递)",Toast.LENGTH_LONG).show();
            Log.e(TAG,"无法通过id找到对应的请假申请(id参数无法传递)");
            finish();
            return;
        }
        if(application!=null){
            title.setText(application.getTitle());//设置请假申请的标题
            Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
            //请假申请内容可能很长,因此用recyclerView来显示
            RecyclerView recyclerView=findViewById(R.id.leave_apply_content_recyclerView);
            LeaveApplyContentAdapter adapter=new LeaveApplyContentAdapter(application);
            Util.loadRecyclerView(recyclerView,new LinearLayoutManager(this),adapter);
            //用recyclerView显示请假申请内容
            application.updateStatus(adapter,this);//更新请假申请的状态
            addEvent();
        }else{
            Toast.makeText(this,"无法通过id找到对应的请假申请(找不到)",Toast.LENGTH_LONG).show();
            Log.e(TAG,"无法通过id找到对应的请假申请(找不到)");
            finish();
        }
    }
    private void addEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回按钮
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除请假申请
                //显示提示窗口
                final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(LeaveApplyContentActivity.this);
                alterDiaglog.setTitle("警告");//标题
                alterDiaglog.setMessage("是否要删除这条请假申请？");//提示消息
                alterDiaglog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(application.getStatus()==LeaveApplication.REVIEWING){
                            LeaveApplication.deleteApplication(application.getApplication_id(),LeaveApplyContentActivity.this);
                        }else{
                            Toast.makeText(LeaveApplyContentActivity.this,"只能删除审核中的请假申请",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alterDiaglog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alterDiaglog.show();
                //显示提示窗口
            }
        });
    }

    public static void actionStart(Context context,LeaveApplication application){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_LEAVE_APPLICATION_CONTENT" );
        intent.putExtra("id",application.getApplication_id());
        context.startActivity(intent);
    }
}
