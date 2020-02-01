package com.chen.handle;


import android.content.Context;
import android.content.SharedPreferences;

import com.chen.data.LeaveApplication;

//数据输入类
//用于从本地或网络获取数据
public class DataInput {
    public static String getNowVersion(){
        String version="";
        //获取版本号(在此书写逻辑)
        version="1.00";
        //获取版本号(在此书写逻辑)
        return version;
    }
    public static String getClassNumber(){
        String number="";
        //获取班号(在此书写逻辑)
        number="2019040000";
        //获取班号(在此书写逻辑)
        return number;
    }

    //从本地获取用户创建的请假申请
    public static String getNowApplication(Context context,String key){
        String result;
        SharedPreferences pref=context.getSharedPreferences("now_application",Context.MODE_PRIVATE);
        result=pref.getString(key,null);
        if (result!=null&&result.equals("")){
            result=null;
        }
        return result;
    }
}
