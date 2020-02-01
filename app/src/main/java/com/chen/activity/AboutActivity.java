package com.chen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.chen.handle.DataInput;

//"关于"板块的activity
public class AboutActivity extends BaseActivity {
    private TextView title;//标题栏的标签
    private TextView version;//显示版本的标签
    private Button update_btn;//"检查更新"按钮
    private Button advise_btn;//"提交反馈"按钮
    private EditText advise_text;//用户书写反馈的文本框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ActionBar bar=getSupportActionBar();
        if(bar!=null){
            bar.hide();//隐藏系统默认的标题栏
        }
        title=(TextView)findViewById(R.id.action_bar_text);
        version=(TextView)findViewById(R.id.version_text);
        update_btn=(Button)findViewById(R.id.update);
        advise_btn=(Button)findViewById(R.id.advise_btn);
        advise_text=(EditText)findViewById(R.id.advise_text);
        version.setText(DataInput.getNowVersion());
        title.setText("关于");
        addEvent();
    }
    private void addEvent(){
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在此书写"检查更新"的逻辑

                //在此书写"检查更新"的逻辑
            }
        });
        advise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String advice=advise_text.getText().toString();//获取用户的反馈
                if(advice.equals("")){
                    Toast.makeText(AboutActivity.this,"你还没有填写反馈信息",Toast.LENGTH_SHORT);
                }else {
                    //在此书写"提交反馈"的逻辑

                    //在此书写"提交反馈"的逻辑
                }
            }
        });
    }
    public static void actionStart(Context context){
        //调用该方法以启动当前活动
        Intent intent=new Intent("com.chen.MainActivity.ACTION_ABOUT");
        context.startActivity(intent);
    }
}
