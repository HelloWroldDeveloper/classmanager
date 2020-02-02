package com.yang.data;

import java.util.Date;

public class MyDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    public MyDate(){//获取当前时间
        Date now_date = new Date(System.currentTimeMillis());
        year=now_date.getYear();
        month=now_date.getMonth();
        day=now_date.getDay();
        hour=now_date.getHours();
        minute=now_date.getMinutes();
    }

    @Override
    public String toString() {
        StringBuilder date=new StringBuilder(year+"/");
        if (month<10)
            date.append("0");
        date.append(month+"/");
        if(day<10)
            date.append("0");
        date.append(day+" ");
        if(hour<10)
            date.append("0");
        date.append(hour+":");
        if(minute<10)
            date.append("0");
        date.append(minute);
        return date.toString();
    }
}
