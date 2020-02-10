package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chen.adapter.VoteAdapter;
import com.chen.data.Vote;
import com.chen.fragment.VoteList_fragment;
import com.chen.fragment.VoteList_noVote_fragment;
import com.chen.handle.Util;

import java.util.List;

//投票事件列表 activity
public class VoteListActivity extends BaseActivity{
    private TextView now_vote;//"进行中"按钮
    private TextView overdue_vote;//"已过期"按钮
    private TextView me;//"我的"按钮

    private int choose=1;//当前的选择(进行中/已过期/我的)

    private VoteList_fragment fragment;//通知列表所在的碎片

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_list);
        ImageButton back=findViewById(R.id.vote_list_back);
        ImageButton add_vote=findViewById(R.id.vote_list_add);
        now_vote=findViewById(R.id.vote_list_now);
        overdue_vote=findViewById(R.id.vote_list_overdue);
        me=findViewById(R.id.vote_list_me);
        if(Vote.isAllow_update()){
            Vote.setAllow_update(false);
            Vote.updateVote(this,choose,null);
        }
        check_now_vote();
        Util.hideDefaultActionbar(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoteListActivity.this.finish();
            }
        });
        add_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_vote();
            }
        });
        now_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_now_vote();
            }
        });
        overdue_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_overtime_vote();
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_my_vote();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //更新通知列表
        if(Vote.getNowVoteList().size()>0){
            VoteAdapter adapter=fragment.getAdapter();
            Vote.updateVote(this,choose,adapter);
        }
    }

    private void check_now_vote(){
        //"进行中"的点击事件处理
        choose=1;
        now_vote.setTextColor(Color.RED);
        overdue_vote.setTextColor(Color.BLACK);
        me.setTextColor(Color.BLACK);
        List<Vote> now_votes=Vote.getNowVoteList();
        if(now_votes.size()>0){
            fragment=new VoteList_fragment(now_votes,false);
            Util.replaceFragment(VoteListActivity.this,R.id.vote_list_fragment,fragment);
        }else {
            Util.replaceFragment(VoteListActivity.this,R.id.vote_list_fragment,new VoteList_noVote_fragment());
        }
    }
    private void check_overtime_vote(){
        //"已过期"的点击事件处理
        choose=2;
        now_vote.setTextColor(Color.BLACK);
        overdue_vote.setTextColor(Color.RED);
        me.setTextColor(Color.BLACK);
        List<Vote> overdue_votes=Vote.getOverdueVoteList();
        if(overdue_votes.size()>0){
            fragment=new VoteList_fragment(overdue_votes,false);
            Util.replaceFragment(VoteListActivity.this,R.id.vote_list_fragment,fragment);
        }else {
            Util.replaceFragment(VoteListActivity.this,R.id.vote_list_fragment,new VoteList_noVote_fragment());
        }
    }
    private void check_my_vote(){
        //"我的"的点击事件处理
        choose=3;
        now_vote.setTextColor(Color.BLACK);
        overdue_vote.setTextColor(Color.BLACK);
        me.setTextColor(Color.RED);
        List<Vote> my_votes=Vote.getMyVoteList();
        if(my_votes.size()>0){
            fragment=new VoteList_fragment(my_votes,true);
            Util.replaceFragment(VoteListActivity.this,R.id.vote_list_fragment,fragment);
        }else {
            Util.replaceFragment(VoteListActivity.this,R.id.vote_list_fragment,new VoteList_noVote_fragment());
        }
    }
    private void add_vote(){
        //"新建投票"的点击事件处理
        VoteAddActivity.actionStart(this);
    }

    //调用该方法以启动当前活动
    public static void actionStart(Context context){
        Vote.setAllow_update(true);
        Intent intent=new Intent("com.chen.MainActivity.ACTION_VOTE_LIST");
        context.startActivity(intent);
    }
}
