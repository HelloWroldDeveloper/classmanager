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
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.chen.data.ClassSign;
import com.chen.service.Locator;

import java.util.ArrayList;
import java.util.List;

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
        sign=ClassSign.searchClassSign();//从服务器检索对应于当前用户的签到
        ActionBar bar=getSupportActionBar();
        if(bar!=null){
            bar.hide();//隐藏系统默认的标题栏
        }
        if(sign==null){
            setContentView(R.layout.no_class_sign);
            TextView title=(TextView)findViewById(R.id.action_bar_text);
            title.setText("签到");
        }else{
            setContentView(R.layout.class_sign);
            back=(TextView)findViewById(R.id.class_sign_back);
            signed=(TextView)findViewById(R.id.class_sign_already_sign);
            unsigned=(TextView)findViewById(R.id.class_sign_not_sign);
            students=(TextView)findViewById(R.id.class_sign_person_list);
            sign_btn=(Button)findViewById(R.id.class_sign_btn);
            TextView place=(TextView)findViewById(R.id.class_sign_place);
            TextView person=(TextView)findViewById(R.id.class_sign_person);
            place.setText(sign.getPlace());
            person.setText(sign.getInitiator());
            updateList(true);//更新已签到人员列表
            addEvent();
        }
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
            StringBuilder builder=new StringBuilder("");
            for(ClassSign.StudentSign studentSign:studentSigns){
                builder.append(studentSign.getName()+" "+studentSign.getSign_time().toString()+"\n");
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
            String[] p=permissions.toArray(new String[permissions.size()]);
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
                    sign.sign_succeed();
                    Toast.makeText(ClassSignActivity.this,"签到成功 距离"+distance,Toast.LENGTH_SHORT).show();
                }else {
                    sign.sign_fail();
                    Toast.makeText(ClassSignActivity.this,"签到失败 距离"+distance,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent=new Intent("com.chen.MainActivity.ACTION_CLASS_SIGN");
        context.startActivity(intent);
    }
}
