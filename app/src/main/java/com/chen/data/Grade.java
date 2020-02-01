package com.chen.data;


import android.util.Log;

import com.chen.handle.IDGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//成绩上传 实体类
public class Grade {
    public static final int SUBMITTED=1;//状态: 已提交
    public static final int NOT_SUBMITTED=2;//状态: 未提交
    public static final int OVER_DUE=3;//状态: 已过期
    public static final String ID_TAG="Grade";//用于生成id的tag

    private static List<Grade> grades=new ArrayList<Grade>();//成绩上传 列表

    private String test_name;//考试名称
    private String monitor;//统计人
    private int nowScore;//用户填写的成绩
    private int status=NOT_SUBMITTED;//状态
    private int id;
    private MyDate date;//发送日期
    private MyDate deadline;//截止日期

    Grade(String test_name,String monitor,MyDate date,MyDate deadline){
        this.test_name=test_name;
        this.monitor=monitor;
        this.id= IDGenerator.generationID(ID_TAG);
        this.date=date;
        this.deadline=deadline;
    }

    public int getId() {
        return id;
    }
    public int getStatus() {
        return status;
    }
    public int getNowScore() {
        return nowScore;
    }
    public String getExtraMsg() {
        return monitor+" "+date.toString();
    }
    public String getTest_name() {
        return test_name;
    }
    public String getMonitor() {
        return monitor;
    }
    public String getDeadline_msg() {
        return deadline.toString();
    }

    public void setNowScore(int nowScore) {
        this.nowScore = nowScore;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public boolean is_overdue() throws ParseException {
        return MyDate.before_now(deadline);
    }

    public static void initGrades(){
        //初始化成绩列表(在此书写逻辑)
        grades.add(new Grade("000000","陈家耀",new MyDate(1,1,1,1,1),new MyDate(2020,1,28,18,10)));
        for(int i=1;i<=15;i++){
            String test_name="";
            for(int j=1;j<=10;j++)
                test_name+=(""+i);
            Grade grade=new Grade(test_name,"陈家耀",new MyDate(i,i,i,i,i),new MyDate(2020,1,27,18,10));
            grades.add(grade);
        }
        //初始化成绩列表(在此书写逻辑)
    }
    public static List<Grade> getLatestGrades()throws ParseException{
        //获取最新的成绩列表(在此书写逻辑)
        //获取最新的成绩列表(在此书写逻辑)
        for(Grade g:grades){
            if((g.getStatus()!=Grade.OVER_DUE)&&g.is_overdue()){
                g.setStatus(Grade.OVER_DUE);
            }
        }
        return grades;
    }
    public static Grade findGradeByID(int id){
        try{
            getLatestGrades();
        }catch (ParseException e){
            e.printStackTrace();
            Log.e(ID_TAG,"无法解析日期");
        }
        for(Grade grade:grades){
            if(grade.getId()==id){
                return grade;
            }
        }
        return null;
    }
}
