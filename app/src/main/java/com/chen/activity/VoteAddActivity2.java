package com.chen.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chen.data.MyDate;
import com.chen.data.Vote;
import com.chen.data.VoteData;
import com.chen.handle.Util;
import com.chen.view.TimeNumberPicker;

import java.text.ParseException;
import java.util.List;

//添加投票(步骤2) activity
public class VoteAddActivity2 extends BaseActivity{
    private static BaseActivity activity;//上一个活动(即上一步)
    private static final String TAG="voteAddActivity2";

    private VoteData voteData;//包含投票选项和标题的数据
    private TimeNumberPicker[] pickers=new TimeNumberPicker[4];//日期/小时/分钟/最大投票项数 选择器
    private Button ok;//确认

    String[] dates;//日期选择器的日期列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_add2);
        Util.hideDefaultActionbar(this);
        TextView actionbar_title= findViewById(R.id.action_bar_text);
        actionbar_title.setText("创建投票");
        ok=findViewById(R.id.vote_add2_ok);
        voteData=(VoteData) getIntent().getSerializableExtra("vote");
        pickers[0]= findViewById(R.id.vote_add2_date);
        pickers[1]=findViewById(R.id.vote_add2_hour);
        pickers[2]=findViewById(R.id.vote_add2_minute);
        pickers[3]=findViewById(R.id.vote_add2_num);
        for(int i=0;i<=2;i++){
            pickers[i].setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            Util.setNumberPickerDividerColor(pickers[i], Color.BLUE);
            pickers[i].setWrapSelectorWheel(false);
            if(i>=1){
                pickers[i].setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int i) {
                        if(i<=9){
                            return "0"+i;
                        }else {
                            return ""+i;
                        }
                    }
                });
            }
        }
        MyDate now=MyDate.getCurrentDate();
        dates=MyDate.generateDates();
        pickers[0].setDisplayedValues(dates);
        pickers[0].setValue(0);
        pickers[0].setMinValue(0);
        pickers[0].setMaxValue(29);
        pickers[1].setMinValue(0);
        pickers[1].setMaxValue(23);
        pickers[1].setValue(now.getHour());
        pickers[2].setMinValue(0);
        pickers[2].setMaxValue(59);
        pickers[2].setValue(now.getMinute());
        pickers[3].setMinValue(0);
        pickers[3].setMaxValue(voteData.getVote_contents().size());
        pickers[3].setValue(0);
        addEvent();
    }

    private void addEvent(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date=dates[pickers[0].getValue()];
                String[] hm=new String[2];
                String time;
                for(int i=1;i<=2;i++){
                    int k=pickers[i].getValue();
                    hm[i-1]= (k<=9 ? "0"+k : ""+k);
                }
                time=date+"  "+hm[0]+":"+hm[1];
                MyDate deadline=MyDate.parseMyDate(time);
                try{
                    List<String> contents=voteData.getVote_contents();
                    String title=voteData.getTitle();
                    if(MyDate.before_now(deadline)){
                        Toast.makeText(VoteAddActivity2.this,"错误:截止日期比当前时间早",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Vote.create_vote(title,contents,deadline,pickers[3].getValue(),activity,VoteAddActivity2.this);
                }catch (ParseException e){
                    e.printStackTrace();
                    Log.e(TAG, "无法解析日期" );
                }
            }
        });
    }

    public static void actionStart(BaseActivity activity, VoteData voteData){
        Intent intent=new Intent("com.chen.MainActivity.ACTION_VOTE_ADD2");
        intent.putExtra("vote",voteData);
        VoteAddActivity2.activity=activity;
        activity.startActivityForResult(intent,1);
    }
}
