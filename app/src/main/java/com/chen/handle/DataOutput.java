package com.chen.handle;

import android.content.Context;
import android.content.SharedPreferences;

import com.chen.data.LeaveApplication;

//数据输出类
//用于向本地或网络保存数据
public class DataOutput {
    private static final String[] LEAVE_APPLICATION_KEYS=new String[]{LeaveApplication.KEY_TITLE,LeaveApplication.KEY_CONTENT
            ,LeaveApplication.KEY_START_DATE,LeaveApplication.KEY_START_TIME,
            LeaveApplication.KEY_END_DATE,LeaveApplication.KEY_END_TIME};//用于保存和读取用户创建的请假申请的关键字

    //保存用户创建的请假申请到本地
    public static void saveNowApplication(Context context, String key,String data){
        SharedPreferences.Editor editor=context.getSharedPreferences("now_application",Context.MODE_PRIVATE).edit();
        for(String s:LEAVE_APPLICATION_KEYS){
            if(!s.equals(key)){
                String temp=DataInput.getNowApplication(context,s);
                if(temp!=null){
                    editor.putString(s,temp);
                }
            }else {
                editor.putString(key,data);
            }
        }
        editor.apply();
    }
    //清除用户创建的请假申请
    public static void deleteApplication(Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences("now_application",Context.MODE_PRIVATE).edit();
        for(String s:LEAVE_APPLICATION_KEYS){
            editor.putString(s,"");
        }
        editor.apply();
    }
}
