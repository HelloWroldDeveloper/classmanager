package com.chen.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.chen.data.ClassSign;
import com.chen.handle.DataInput;
import com.chen.handle.DataOutput;
import com.chen.handle.Util;
import com.chen.service.Locator;

import java.util.ArrayList;
import java.util.List;

//签到 activity
public class ClassSignActivity extends BaseActivity{
    private TextView back;//返回
    private TextView signed;//已签到
    private TextView unsigned;//未签到
    private TextView students;//签到人员表
    private Button sign_btn;//签到按钮

    private ClassSign sign;//当前签到

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从服务器检索对应于当前用户的签到
        if(ClassSign.isAllow_activity_update()){
            ClassSign.searchClassSign(this);
            ClassSign.setAllow_activity_update(false);
        }
        sign=ClassSign.getNow_sign();
        Util.hideDefaultActionbar(this);//隐藏系统默认的标题栏
        if(sign==null){
            setContentView(R.layout.no_class_sign);
            TextView title=findViewById(R.id.action_bar_text);
            title.setText("签到");
        }else{
            sign.setStatus(DataInput.getSignStatus(this));
            setContentView(R.layout.class_sign);
            back=findViewById(R.id.class_sign_back);
            signed=findViewById(R.id.class_sign_already_sign);
            unsigned=findViewById(R.id.class_sign_not_sign);
            students=findViewById(R.id.class_sign_person_list);
            sign_btn=findViewById(R.id.class_sign_btn);
            TextView place=findViewById(R.id.class_sign_place);
            TextView person=findViewById(R.id.class_sign_person);
            place.setText(sign.getPlace());
            person.setText(sign.getInitiator());
            updateList(true);//更新已签到人员列表
            if(sign.getStatus()==ClassSign.FAILED){
                sign_btn.setEnabled(false);
                sign_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_grey));
                sign_btn.setText("签到失败");
            }else if(sign.getStatus()==ClassSign.SIGNED){
                sign_btn.setEnabled(false);
                sign_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_grey));
                sign_btn.setText("签到成功");
            }
            addEvent();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataOutput.saveSignStatus(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //与申请权限的结果有关的回调方法
        if(requestCode==1){
            if(grantResults.length>0){
                for(int r:grantResults){
                    if(r!=PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"取消权限将无法签到",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                locate();
            }else {
                Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassSignActivity.this.finish();
            }
        });
        signed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signed.setTextColor(Color.RED);
                unsigned.setTextColor(Color.BLACK);
                updateList(true);
            }
        });
        unsigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signed.setTextColor(Color.BLACK);
                unsigned.setTextColor(Color.RED);
                updateList(false);
            }
        });
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //签到按钮的点击事件处理(在此书写逻辑)
                applyPermission();//申请权限并执行定位
                //签到按钮的点击事件处理(在此书写逻辑)
            }
        });
    }
    private void updateList(boolean signed_students){
        //更新签到人员列表
        List<ClassSign.StudentSign> studentSigns=signed_students ? sign.getSignedStudents():sign.getUnsignedStudents();
        if(studentSigns==null){
            students.setText("当前为空白...");
        }else{
            StringBuilder builder=new StringBuilder();
            for(ClassSign.StudentSign studentSign:studentSigns){
                builder.append(studentSign.getName());
                builder.append("\n");
            }
            students.setText(builder.toString());
        }
    }
    private void applyPermission(){
        //判断一下必要的三个权限是否都被允许了
        String[] permissions_to_apply=new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> permissions=new ArrayList<>();
        for(String permission:permissions_to_apply){
            if(ContextCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED){
                permissions.add(permission);
            }
        }
        //判断一下必要的三个权限是否都被允许了
        if(!permissions.isEmpty()){
            //申请权限
            String[] p=permissions.toArray(new String[0]);
            ActivityCompat.requestPermissions(this,p,1);
        }else {
            //执行定位
            locate();
        }
    }
    private void locate(){
        //进行定位并判断是否签到成功
        Locator.locate(getApplicationContext(), null, new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                LatLng latLng =new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()),
                        latLng2=new LatLng(sign.getCenter()[0],sign.getCenter()[1]);
                SDKInitializer.initialize(getApplicationContext());
                double distance= DistanceUtil.getDistance(latLng,latLng2);
                if(distance<=sign.getDistance()){
                    sign.sign((int)distance,true,sign_btn,ClassSignActivity.this);

                }else {
                    sign.sign((int)distance,false,sign_btn,ClassSignActivity.this);
                }
            }
        });
    }

    public static void actionStart(Context context){
        ClassSign.setAllow_activity_update(true);
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_SIGN");
        context.startActivity(intent);
    }
}
