package com.chen.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.chen.adapter.VoteContentAdapter;
import com.chen.data.MyDate;
import com.chen.data.Vote;
import com.chen.adapter.VoteContentAdapter2;
import com.chen.handle.Util;

import java.text.ParseException;

//投票详情 activity
public class VoteContentActivity extends BaseActivity{
    private static String TAG="Vote_content";

    private Vote vote;//当前投票事件

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        final int vote_id=intent.getIntExtra("vote_id",-1);
        boolean is_me=intent.getBooleanExtra("is_me",false);
        vote=Vote.findVoteByID(vote_id);
        Util.hideDefaultActionbar(this);
        if(vote!=null){
            String title=formatTitle();
            boolean is_overdue=false;
            try{
                is_overdue=MyDate.before_now(vote.getDeadline());
            }catch (ParseException e){
                e.printStackTrace();
                Log.e(TAG, "无法解析日期");
            }
            TextView vote_title;
            if(vote.isVoted()||is_overdue){
                //如果已投或者过期
                setContentView(R.layout.vote_content2);
                vote_title=findViewById(R.id.vote_content2_title);
                RecyclerView recyclerView=findViewById(R.id.vote_content2_recyclerview);
                Util.loadRecyclerView(recyclerView,new LinearLayoutManager(this),new VoteContentAdapter2(vote.getChoices()));//加载recyclerView
            }else {
                //如果未投并且未过期
                setContentView(R.layout.vote_content1);
                vote_title=findViewById(R.id.vote_content1_title);
                TextView actionbar_title=findViewById(R.id.vote_content1_actionbar_title);
                actionbar_title.setText("投票详情(最多可投"+vote.getMax_select()+"项)");
                RecyclerView recyclerView=findViewById(R.id.vote_content1_recyclerview);
                final VoteContentAdapter adapter=new VoteContentAdapter(vote.getMax_select(),vote.getChoices());
                Util.loadRecyclerView(recyclerView,new LinearLayoutManager(this),adapter);//加载recyclerView
                Button execute_vote=findViewById(R.id.vote_content1_btn);
                execute_vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //投票按钮
                        Vote.execute_vote(VoteContentActivity.this,vote,adapter.getChoices());
                    }
                });
            }
            if(is_me){
                //如果是由当前用户创建的投票
                TextView delete=findViewById(R.id.vote_content_delete);
                delete.setVisibility(View.VISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //删除投票 按钮
                        //显示提示窗口
                        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(VoteContentActivity.this);
                        alterDiaglog.setTitle("警告");//标题
                        alterDiaglog.setMessage("是否要删除这条投票？");//提示消息
                        alterDiaglog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    if(MyDate.before_now(vote.getDeadline())){
                                        Toast.makeText(VoteContentActivity.this,"无法删除已过期的投票",Toast.LENGTH_SHORT).show();
                                    } else{
                                        Vote.deleteVote(vote_id,VoteContentActivity.this);
                                    }
                                }catch (ParseException e){
                                    e.printStackTrace();
                                    Log.e(TAG, "无法解析日期" );
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
            vote_title.setText(title);//设置投票标题
            //返回按钮
            ImageButton back=findViewById(R.id.action_bar_btn);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            //返回按钮
        }
    }

    private String formatTitle(){
        //格式化投票标题
        String title=vote.getTitle();
        int length=title.length();
        if(length<=12){
            return title;
        }else{
            String p1=title.substring(0,12);
            String p2;
            if(length<=20){
                p2=title.substring(12,length);
                return p1+"\n"+p2;
            }else{
                p2=title.substring(12,19);
                return p1+"\n"+p2+"...";
            }
        }
    }
    public static void actionStart(Activity activity, int vote_id, boolean is_me){
        Intent intent=new Intent("com.chen.MainActivity.ACTION_VOTE_CONTENT");
        intent.putExtra("vote_id",vote_id);
        intent.putExtra("is_me",is_me);
        activity.startActivity(intent);
    }
}
