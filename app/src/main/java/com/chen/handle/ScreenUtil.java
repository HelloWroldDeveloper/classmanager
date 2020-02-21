package com.chen.handle;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class ScreenUtil {
    public static void adapterScreen(Activity activity,int targetDP,boolean isVertical) {
        //系统的屏幕尺寸
        DisplayMetrics systemDM = Resources.getSystem().getDisplayMetrics();
        //app整体的屏幕尺寸
        DisplayMetrics appDM = activity.getApplication().getResources().getDisplayMetrics();
        //activity的屏幕尺寸
        DisplayMetrics activityDM = activity.getResources().getDisplayMetrics();
        if (isVertical) {
            // 适配屏幕的高度
            activityDM.density = (float) (activityDM.heightPixels / (targetDP*1.0));
        } else {
            // 适配屏幕的宽度
            activityDM.density = (float)(activityDM.widthPixels / (targetDP*1.0)) ;
        }
        // 适配相应比例的字体大小
        activityDM.scaledDensity = activityDM.density * (systemDM.scaledDensity / systemDM.density);
        // 适配dpi
        activityDM.densityDpi = (int)(160 * activityDM.density);
    }

    public static void resetScreen(Activity activity) {
        //系统的屏幕尺寸
        DisplayMetrics systemDM = Resources.getSystem().getDisplayMetrics();
        //app整体的屏幕尺寸
        DisplayMetrics appDM = activity.getApplication().getResources().getDisplayMetrics();
        //activity的屏幕尺寸
        DisplayMetrics activityDM = activity.getResources().getDisplayMetrics();

        activityDM.density = systemDM.density;
        activityDM.scaledDensity = systemDM.scaledDensity;
        activityDM.densityDpi = systemDM.densityDpi;
        appDM.density = systemDM.density;
        appDM.scaledDensity = systemDM.scaledDensity;
        appDM.densityDpi = systemDM.densityDpi;
    }
}
