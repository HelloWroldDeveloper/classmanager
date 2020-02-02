package com.yang.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.yang.adapter.QuestionItemAdapter;
import com.yang.data.QuestionItem;

import com.chen.activity.MainActivity;
import com.chen.activity.R;

public class Main2Activity extends AppCompatActivity {

    // questionList用于存储数据
    private List<QuestionItem> questionItemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {//点击返回按钮跳转到主页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button back =(Button)findViewById(R.id.backtomain);
        back.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){
                Main2Activity.this.finish();
            }
        });
        Button startvote =(Button)findViewById(R.id.startvote);//点击开始投票按钮跳转到发起投票页面
        startvote.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){
                Intent intent =new Intent();
                intent.setClass(Main2Activity.this,VoteContentActivity.class);
                startActivity(intent);
                Main2Activity.this.finish();
            }
        });
        // 先拿到数据并放在适配器上
        initQuestionList(); //初始化问题数据
        QuestionItemAdapter adapter=new QuestionItemAdapter(Main2Activity.this,R.layout.layout_question_item,questionItemList);

        // 将适配器上的数据传递给listView
        ListView listView=findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionItem questionItem=questionItemList.get(position);
                Toast.makeText(Main2Activity.this,questionItem.getQuestion(),Toast.LENGTH_SHORT).show();
                //从数据库读取时间和现实时间比对，跳转到投票界面和现实结果界面，如果在截止日期前，跳转到VoteActivity.class否则跳转到VoteResultActivity
                Intent intent =new Intent();
                intent.setClass(Main2Activity.this,VoteActivity.class);
            }
        });

    }

    // 初始化数据
    private void initQuestionList(){
        for(int i=0;i<10;i++){
            String questionName;
            if (i<10) {
                questionName ="0"+i;
            } else {
                questionName = ""+i;
            }
    //从数据库读取问题和截止日期
            String questionChoise1=questionName+"Choice1";
            String questionChoise2=questionName+"Choice2";
            String questionChoise3=questionName+"Choice3";
            String questionDateTime="2020/02/02 10:25";
            QuestionItem questionItem=new QuestionItem(questionName, questionDateTime,questionChoise1,questionChoise2,questionChoise3,i+1, -1);
            questionItemList.add(questionItem);
        }
    }
}
