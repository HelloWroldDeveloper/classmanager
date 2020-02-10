package com.chen.data;

public class AppVersion {
    private String version;//版本
    private String update;//更新内容
    private MyDate date;//更新日期

    public AppVersion(String version,String update,String date){
        this.version=version;
        this.update=update;
        this.date=MyDate.parseMyDate(date);
    }

    public MyDate getDate() {
        return date;
    }
    public String getUpdate() {
        return update;
    }
    public String getVersion() {
        return version;
    }
}
