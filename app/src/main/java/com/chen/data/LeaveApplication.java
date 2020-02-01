package com.chen.data;

import android.util.Log;

import com.chen.handle.IDGenerator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//请假申请 实体类
public class LeaveApplication {
    public static final int REVIEWING=1;//状态:审核中
    public static final int PASSED=2;//状态:已通过
    public static final int NO_PASS=3;//状态:未通过
    public static final int IGNORED=4;//状态:已忽略
    public static final int OVER_DUE=5;//状态:已过期
    public static final String ID_TAG="LeaveApplication";//用于生成id的tag

    //用于保存和读取数据的关键字
    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_START_DATE="start_date";
    public static final String KEY_END_DATE="end_date";
    public static final String KEY_START_TIME="start_time";
    public static final String KEY_END_TIME="end_time";
    //用于保存和读取数据的关键字

    private static List<LeaveApplication> applications=new ArrayList<>();//请假申请 列表

    private String title;
    private String content;
    private MyDate start_date;
    private MyDate end_date;
    private MyDate create_date;
    private int status;
    private int id;

    public LeaveApplication(String title,String content,MyDate start_date,MyDate end_date){
        this.title=title;
        this.content=content;
        this.start_date=start_date;
        this.end_date=end_date;
        this.status=LeaveApplication.REVIEWING;
        this.create_date=MyDate.getCurrentDate();
        this.id= IDGenerator.generationID(ID_TAG);
    }

    public String getTitle(){
        return  title;
    }
    public String getContent() {
        return content;
    }
    public MyDate getStart_date() {
        return start_date;
    }
    public MyDate getEnd_date() {
        return end_date;
    }
    public MyDate getCreate_date() {
        return create_date;
    }
    public int getStatus() {
        return status;
    }
    public int getId() {
        return id;
    }
    public String getStatusStatement(){
        switch (status){
            case LeaveApplication.REVIEWING:
                return "审核中";
            case LeaveApplication.PASSED:
                return "已通过";
            case LeaveApplication.NO_PASS:
                return "未通过";
            case LeaveApplication.IGNORED:
                return "已忽略";
            case LeaveApplication.OVER_DUE:
                return "已过期";
            default:
                return null;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static void initApplications(){
        //初始化请假申请列表(在此书写逻辑)
        MyDate date1=new MyDate(2020,1,1,1,1);
        MyDate date2=new MyDate(2020,1,2,2,2);
        for (int i=1;i<=50;i++){
            String content="";
            for (int k=1;k<=600;k++){
                content+=(""+i);
            }
            LeaveApplication leave=new LeaveApplication(i+""+i,content,date1,date2);
            applications.add(leave);
        }
        //初始化请假申请列表(在此书写逻辑)
    }
    public static List<LeaveApplication> getLatestApplications(){
        //获取最新的请假申请列表(在此书写逻辑)
        try{
            for(LeaveApplication application:applications){
                if((application.getStatus()!=LeaveApplication.OVER_DUE)&&MyDate.before_now(application.getEnd_date())){
                    application.setStatus(LeaveApplication.OVER_DUE);
                }else if((application.getStatus()==LeaveApplication.REVIEWING)&&MyDate.before_now(application.getStart_date())){
                    application.setStatus(LeaveApplication.IGNORED);
                }
            }
        }catch (ParseException e){
            e.printStackTrace();
            Log.e(ID_TAG,"无法解析日期");
        }
        return applications;
        //获取最新的请假申请列表(在此书写逻辑)
    }
    //通过id查找 请假申请
    public static LeaveApplication findApplicationByID(int id){
        getLatestApplications();
        for(LeaveApplication application:applications){
            if(application.getId()==id){
                return application;
            }
        }
        return null;
    }
    //添加新的请假申请
    public static void addNewApplication(LeaveApplication application){
        applications.add(application);
    }
}
