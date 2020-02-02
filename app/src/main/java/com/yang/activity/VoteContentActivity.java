package com.yang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.yang.data.MyDate;
import com.chen.activity.R;
import com.yang.data.QuestionItem;

public class VoteContentActivity extends AppCompatActivity {
    private static final String TAG="VoteContent";//用于发日志的tag
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_content);
        Button back=(Button)findViewById(R.id.backtomain2);
        back.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){
                Intent intent =new Intent();
                intent.setClass(VoteContentActivity.this,Main2Activity.class);
                startActivity(intent);
                VoteContentActivity.this.finish();
            }
        });

        Button submit=(Button)findViewById(R.id.questionsubmit);

        EditText deadline =(EditText)findViewById(R.id.deadline);
        EditText choice1=(EditText)findViewById(R.id.choice1);


        submit.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){//点击提交按钮，将文本框数据取出，生成框，并跳回主页
                String txtStartVote=((EditText)findViewById(R.id.startvote1)).getText().toString();
                String txtDeadline=((EditText)findViewById(R.id.deadline)).getText().toString();
                Log.i(TAG, "txtStartVote="+txtStartVote+", txtDeadline="+txtDeadline);
                String txtChoice1=((EditText)findViewById(R.id.choice1)).getText().toString();
                String txtChoice2=((EditText)findViewById(R.id.choice2)).getText().toString();
                String txtChoice3=((EditText)findViewById(R.id.choice3)).getText().toString();
                if(txtStartVote.compareTo("")!=0&&txtDeadline.compareTo("")!=0)
                {
                    MyDate myDate=new MyDate();

                    if(txtDeadline.compareTo(myDate.toString())>0)//比较输入数据的合法性
                    {
                        QuestionItem questionandChoice =new QuestionItem(txtStartVote,txtDeadline,txtChoice1,txtChoice2,txtChoice3,1,-1);//数据需要存储到数据库
                        Intent intent =new Intent();
                        intent.setClass(VoteContentActivity.this,Main2Activity.class);
                        startActivity(intent);
                        VoteContentActivity.this.finish();
                    }
                    else
                        {
                            Toast ts = Toast.makeText(getBaseContext(),"时间无效",Toast.LENGTH_LONG);
                            ts.show();
                        }
                }
                else if(txtStartVote.compareTo("")==0)//判断投票问题是否为空
                    {
                        Toast ts = Toast.makeText(getBaseContext(),"发起问题为空",Toast.LENGTH_LONG);
                        ts.show();
                    }
                else//判断截止日期是否为空
                    {
                        Toast ts = Toast.makeText(getBaseContext(),"截止日期为空",Toast.LENGTH_LONG);
                        ts.show();
                    }
            }
        });



    }
}
