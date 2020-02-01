package com.chen.data;

import java.util.ArrayList;
import java.util.List;
//通知 实体类
public class Notice {
    private static ArrayList<Notice> notices=new ArrayList<Notice>();

    private String title;//通知标题
    private String content;//通知内容
    private String author;//发送人
    private MyDate date;//发送日期
    private boolean read=false;//是否已读

    Notice(String title,String content,String author,MyDate date){
        this.title=title;
        this.content=content;
        this.author=author;
        this.date=date;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getExtraMsg(){
        return author+"  "+date.toString();
    }
    public void read(){
        read=true;
    }
    public boolean isRead() {
        return read;
    }

    public static void initNotices(){
        //初始化通知列表(在此书写逻辑)
        for(int i=1;i<=20;i++) {
            String t = "", c = "";
            for (int k = 1; k <= 30; k++) {
                t += (i + "");
            }
            for (int k = 1; k <= 1000; k++) {
                c += (i + "");
            }
            Notice n = new Notice(t, c, "陈家耀", new MyDate(2020, 1, 23, 14, 23));
            notices.add(n);
        }
        //初始化通知列表(在此书写逻辑)
    }
    public static List<Notice> getLatestNotices(){
        //获取最新的通知列表(在此书写逻辑)
        //获取最新的通知列表(在此书写逻辑)
        for(int i=0;i<notices.size();i++){
            Notice n=notices.get(i);
            if(n.isRead()){
                notices.remove(i);
                notices.add(n);
            }
        }
        return notices;
    }
}
