package com.yang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.chen.activity.R;

public class VoteResultActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_result);
        // 设置投票选项，需要得到问题和投票选项信息,最后的投票结果
        TextView question = (TextView) findViewById(R.id.resultquestion);
        TextView  choice1 = (TextView) findViewById(R.id.resultchoice1);
        TextView  choice2 = (TextView) findViewById(R.id.resultchoice2);
        TextView  choice3 = (TextView) findViewById(R.id.resultchoice3);
        TextView  number1 = (TextView) findViewById(R.id.number1);
        TextView  number2 = (TextView) findViewById(R.id.number2);
        TextView  number3 = (TextView) findViewById(R.id.number3);
        number1.setText("3");
        number2.setText("2");
        number3.setText("1");
        question.setText("Question");
        choice1.setText("Choice1");
        choice2.setText("Choice2");
        choice3.setText("Choice3");
    }
}
