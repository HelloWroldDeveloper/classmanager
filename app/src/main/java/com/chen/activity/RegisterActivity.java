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

import com.chen.handle.DataOutput;
import com.chen.handle.Util;

public class RegisterActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate);
        Util.hideDefaultActionbar(this);
        final EditText name=findViewById(R.id.name);
        final EditText activate_code=findViewById(R.id.activate_code);
        TextView title=findViewById(R.id.action_bar_text);
        title.setText("邮箱激活");
        Button activate=findViewById(R.id.activate);
        Intent intent=getIntent();
        final String number=intent.getStringExtra("number");
        final String email=intent.getStringExtra("email");
        final String password=intent.getStringExtra("password");
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n=name.getText().toString();
                String a=activate_code.getText().toString();
                if(n.equals("")){
                    Toast.makeText(RegisterActivity.this,"请填写姓名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (a.equals("")){
                    Toast.makeText(RegisterActivity.this,"请填写激活码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(a.length()!=6){
                    Toast.makeText(RegisterActivity.this,"激活码是6位的",Toast.LENGTH_SHORT).show();
                    return;
                }
                DataOutput.user_activate(number,n,password,email,a,RegisterActivity.this);
            }
        });
    }

    public static void actionStart(String number, String email, String password, Context context){
        Intent intent=new Intent("com.chen.MainActivity.ACTION_REGISTER");
        intent.putExtra("number",number);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }
}
