package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chen.data.VoteData;
import com.chen.fragment.Vote_add_fragment1;
import com.chen.fragment.Vote_add_fragment2;
import com.chen.handle.DataInput;
import com.chen.handle.DataOutput;
import com.chen.handle.Util;

import java.util.ArrayList;
import java.util.List;

//添加投票(步骤1) activity
public class VoteAddActivity extends BaseActivity{
    private List<String> vote_contents;//投票选项列表
    private Vote_add_fragment1 fragment;//投票选项列表 碎片
    private boolean creating=false;//成功创建投票的标志

    private Button add_vote;//添加选项
    private Button delete_vote;//删除选项
    private Button ok;//确认
    private EditText vote_title;//投票标题

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_add);
        Util.hideDefaultActionbar(this);
        VoteData voteData=DataInput.getNowVoteData();
        vote_contents= voteData.getVote_contents();
        add_vote=findViewById(R.id.vote_add_add);
        delete_vote=findViewById(R.id.vote_add_delete);
        ok=findViewById(R.id.vote_add_ok);
        vote_title=findViewById(R.id.vote_add_title);
        vote_title.setText(voteData.getTitle());
        TextView title=findViewById(R.id.action_bar_text);
        title.setText("创建投票");
        if(vote_contents.size()>0){
            fragment=new Vote_add_fragment1(vote_contents);
            Util.replaceFragment(this,R.id.vote_add_fragment,fragment);
        }else {
            Util.replaceFragment(this,R.id.vote_add_fragment,new Vote_add_fragment2());
        }
        addEvent();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(creating){return;}
        if(vote_contents.size()>0) {
            vote_contents = fragment.getContents();
        }
        DataOutput.saveNowVote(new VoteData(vote_contents,vote_title.getText().toString()));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                boolean quit=data.getBooleanExtra("return",false);
                if(quit){
                    Toast.makeText(this,"创建投票成功",Toast.LENGTH_SHORT).show();
                    creating=true;
                    this.finish();
                }
            }
        }
    }

    private void addEvent(){
        add_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加投票选项
                if(vote_contents.size()==0){
                    vote_contents.add("");
                    fragment=new Vote_add_fragment1(vote_contents);
                    Util.replaceFragment(VoteAddActivity.this,R.id.vote_add_fragment,fragment);
                }else{
                    vote_contents=fragment.addContent();
                }
            }
        });
        delete_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除投票选项
                if(vote_contents.size()==0){
                    Toast.makeText(VoteAddActivity.this,"你还没有添加选项呢",Toast.LENGTH_SHORT).show();
                }else{
                    vote_contents=fragment.deleteContent();
                    if(vote_contents.size()==0){
                        Util.replaceFragment(VoteAddActivity.this,R.id.vote_add_fragment,new Vote_add_fragment2());
                    }
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认按钮
                if(vote_contents.size()>0){
                    vote_contents=fragment.getContents();
                    List<String> choices=new ArrayList<>();
                    String title;
                    for (int i=0;i<vote_contents.size();i++){
                        String content=vote_contents.get(i);
                        if(content.equals("")){
                            Toast.makeText(VoteAddActivity.this,"选项"+(i+1)+"还没有填写呢",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            choices.add(content);
                        }
                    }
                    title=vote_title.getText().toString();
                    if(title.equals("")){
                        Toast.makeText(VoteAddActivity.this,"你还没有填写投票标题呢",Toast.LENGTH_SHORT).show();
                    }else {
                        VoteAddActivity2.actionStart(VoteAddActivity.this,new VoteData(choices,title));
                    }
                }else{
                    Toast.makeText(VoteAddActivity.this,"你还没有添加选项呢",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //调用该方法以启动当前活动
    public static void actionStart(Context context){
        Intent intent=new Intent("com.chen.MainActivity.ACTION_VOTE_ADD");
        context.startActivity(intent);
    }
}
