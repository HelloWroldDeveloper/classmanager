package com.yang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.activity.R;

public class VoteActivity extends AppCompatActivity {
    private int user_choice=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {//点击返回按钮返回到投票主页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        Button back=(Button)findViewById(R.id.voteback);
                back.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){
                Intent intent =new Intent();
                intent.setClass(VoteActivity.this,Main2Activity.class);
                startActivity(intent);
                VoteActivity.this.finish();
            }
        });
        Button submit=(Button)findViewById(R.id.submitvote);
        submit.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){
                //读取用户id，改变用户状态

                //读取用户id，改变用户状态
                RadioButton c1 =(RadioButton)findViewById(R.id.radioButton1);
                RadioButton c2 =(RadioButton)findViewById(R.id.radioButton2);
                RadioButton c3 =(RadioButton)findViewById(R.id.radioButton3);
                if(c1.isChecked()){
                    user_choice=1;
                }
                else if(c2.isChecked()){
                    user_choice=2;
                }
                else if(c3.isChecked()) {
                    user_choice=3;
                }
                else {
                    Toast ts = Toast.makeText(getBaseContext(),"请选择一个选项",Toast.LENGTH_LONG);
                    ts.show();
                    return;
                    }
                //将用户id，问题id，用户选项传入库中


                //将用户id，问题id，用户选项传入库中
                Intent intent =new Intent();
                intent.setClass(VoteActivity.this,Main2Activity.class);
                startActivity(intent);
                VoteActivity.this.finish();
            }
        });

        // 设置投票选项，需要得到问题和投票选项信息
        TextView question =(TextView)findViewById(R.id.votequestion) ;
        RadioButton choice1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton choice2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton choice3 = (RadioButton) findViewById(R.id.radioButton3);
        question.setText("Question");
        choice1.setText("Choice1");
        choice2.setText("Choice2");
        choice3.setText("Choice3");
    }
}
