package com.chen.data;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//日期 实体类
public class MyDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    public MyDate(int year,int month,int day,int hour,int minute){
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=hour;
        this.minute=minute;
    }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }

    //把当前MyDate对象向前递推1天
    private void toNextDay(){
        day++;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (day==32){
                    day=1;
                    month++;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if(day==31){
                    day=1;
                    month++;
                }
                break;
            case 2:
                if((year%4!=0)||(year%100==0&&year%400!=0)){
                    //当前年不是闰年
                    if(day==29){
                        day=1;
                        month++;
                    }
                }else{
                    //当前年是闰年
                    if(day==30){
                        day=1;
                        month++;
                    }
                }
                break;
        }
        if(month==13){
            month=1;
            year++;
        }
    }

    //得到MyDate对象的日期描述
    private String getDateDescription(){
        StringBuilder date=new StringBuilder(year+"年");
        if (month<10)
            date.append("0");
        date.append(month);
        date.append("月");
        if(day<10)
            date.append("0");
        date.append(day);
        date.append("日");
        return date.toString();
    }

    //得到MyDate对象的完整描述
    @Override
    @NonNull
    public String toString() {
        StringBuilder date=new StringBuilder(year+"年");
        if (month<10)
            date.append("0");
        date.append(month);
        date.append("月");
        if(day<10)
            date.append("0");
        date.append(day);
        date.append("日  ");
        if(hour<10)
            date.append("0");
        date.append(hour);
        date.append(":");
        if(minute<10)
            date.append("0");
        date.append(minute);
        return date.toString();
    }
    //由字符串解析为日期对象
    //格式为 yyyy年mm月dd日  HH:MM
    public static MyDate parseMyDate(String date){
        int y;
        int[] mdhm=new int[4];
        String[] temp=new String[4];
        y=Integer.parseInt(date.substring(0,4));
        temp[0]=date.substring(5,7);
        temp[1]=date.substring(8,10);
        temp[2]=date.substring(13,15);
        temp[3]=date.substring(16);
        for(int i=0;i<4;i++){
            String s=temp[i];
            char[] c=s.toCharArray();
            mdhm[i]=(c[0]-48)*10+c[1]-48;
        }
        return new MyDate(y,mdhm[0],mdhm[1],mdhm[2],mdhm[3]);
    }

    //判断当前时间是否比所提供的时间早(包括刚好相等)
    public boolean before(MyDate date) throws ParseException {
        Date from = new SimpleDateFormat("yyyy年MM月dd日  HH:mm").parse(date.toString());
        Date to=  new SimpleDateFormat("yyyy年MM月dd日  HH:mm").parse(this.toString());
        if(this.year==date.year&&this.month==date.month&&this.day==date.day
        &&this.hour==date.hour&&this.minute==date.minute){
            return true;
        }else{
            return (from.getTime()-to.getTime())>0;
        }
    }
    //获取当前时间
    public static MyDate getCurrentDate(){
        Calendar calendars = Calendar.getInstance();
        calendars.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return new MyDate(calendars.get(Calendar.YEAR),calendars.get(Calendar.MONTH)+1,calendars.get(Calendar.DATE)
                ,calendars.get(Calendar.AM_PM)==Calendar.AM ? calendars.get(Calendar.HOUR):calendars.get(Calendar.HOUR)+12,calendars.get(Calendar.MINUTE));
    }
    //判断给定的日期是否在当前日期之前(包括刚好相等)
    public static boolean before_now(MyDate date) throws ParseException {
        return date.before(getCurrentDate());
    }
    //生成30天内的日期描述
    public static String[] generateDates(){
        String[] dates=new String[30];
        MyDate now=MyDate.getCurrentDate();
        dates[0]=now.getDateDescription();
        for(int i=1;i<=29;i++){
            now.toNextDay();
            dates[i]=now.getDateDescription();
        }
        return dates;
    }
}
