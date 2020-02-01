package com.yang.inter;

import com.chen.data.MyDate;
import com.chen.data.User;
import com.chen.handle.DataInput;
import com.chen.handle.IDGenerator;

import java.text.ParseException;
import java.util.List;

//项目接口 实用类
public class AppInterface {
    public static void AppInit(){
        //该方法在主活动启动时会被调用(可以在此书写"初始化"逻辑)

        //该方法在主活动启动时会被调用(可以在此书写"初始化"逻辑)
    }

    public static int getID(String name_space){
        //根据传入的命名空间获取唯一的id
        return IDGenerator.generationID(name_space);
    }
    public static String getClassNumber(){
        //获取班号
        return DataInput.getClassNumber();
    }
    public static String getAppVersion(){
       //获取app版本号
       return DataInput.getNowVersion();
    }
    public static User getNowUser(){
        //获取当前用户
        return User.getNowUser();
    }
    public static List<User> getClassUsers(){
        //获取班级所有的用户
        return User.getLatestUsers();
    }

    public static MyDate getCurrentDate(){
        //获取当前时间
        return MyDate.getCurrentDate();
    }
    public static boolean before_now(MyDate date) throws ParseException {
        //判断给定的日期是否在当前系统日期之前(包括刚好相等)
        return MyDate.before_now(date);
    }
    public static boolean compare_date(MyDate date1,MyDate date2)throws ParseException{
        //判断date1是否比date2早(包括刚好相等)
        return date1.before(date2);
    }
}
