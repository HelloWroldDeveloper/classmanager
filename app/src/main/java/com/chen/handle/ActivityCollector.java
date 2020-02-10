package com.chen.handle;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
//活动 列表类
public class ActivityCollector {
    private static List<Activity> activities=new ArrayList<>();//当前所有的活动

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    /*public static void finishAll(){
        for (Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }*/
    static List<Activity> getActivities() {
        return activities;
    }
}
