package com.chen.data;

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

    @Override
    public String toString() {
        StringBuilder date=new StringBuilder(year+"年");
        if (month<10)
            date.append("0");
        date.append(month+"月");
        if(day<10)
            date.append("0");
        date.append(day+"日  ");
        if(hour<10)
            date.append("0");
        date.append(hour+":");
        if(minute<10)
            date.append("0");
        date.append(minute);
        return date.toString();
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
        MyDate now=new MyDate(calendars.get(Calendar.YEAR),calendars.get(Calendar.MONTH)+1,calendars.get(Calendar.DATE)
        ,calendars.get(Calendar.AM_PM)==Calendar.AM ? calendars.get(Calendar.HOUR):calendars.get(Calendar.HOUR)+12,calendars.get(Calendar.MINUTE));
        return now;
    }
    //判断给定的日期是否在当前日期之前(包括刚好相等)
    public static boolean before_now(MyDate date) throws ParseException {
        return date.before(getCurrentDate());
    }
}
