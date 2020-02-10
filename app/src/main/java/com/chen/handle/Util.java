package com.chen.handle;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import java.lang.reflect.Field;

//实用工具类
public class Util {
    private static final String TAG="Util";

    /**
     * 将一个碎片加载到特点的帧布局上
     * @param activity 帧布局所在的活动
     * @param fragmentLayout_id 帧布局的id
     * @param fragment 被加载的碎片
     */
    public static void replaceFragment(FragmentActivity activity, int fragmentLayout_id, Fragment fragment){
        FragmentManager manager=activity.getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(fragmentLayout_id,fragment);
        transaction.commit();
    }
    /**
     * 将某个活动的系统标题栏隐藏
     * @param activity 要隐藏标题栏的活动
     */
    public static void hideDefaultActionbar(AppCompatActivity activity){
        ActionBar bar=activity.getSupportActionBar();
        if(bar!=null){
            bar.hide();//隐藏系统默认的标题栏
        }
    }
    /**
     *加载某个recyclerView
     * @param recyclerView 被加载的recyclerView
     * @param layoutManager recyclerView的布局管理器
     * @param adapter recyclerView的适配器
     * @return 加载好的recyclerView
     */
    public static RecyclerView loadRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }
    /**
     *设置某个NumberPicker的分割线颜色
     * @param number 要设置分割线颜色的NumberPicker
     * @param color 分割线颜色
     */
    public static void setNumberPickerDividerColor(NumberPicker number,int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(number, new ColorDrawable(color));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "setNumberPickerDividerColor: 无法设置分割线颜色");
                }
                break;
            }
        }
    }

    /**
     * 重新加载某个活动
     * @param activity 要重新加载的活动
     */
    public static void recreateActivity(@org.jetbrains.annotations.NotNull Activity activity){
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 用于在发生http请求错误时显示错误
     * @param activity http请求所操作的活动
     * @param e 发生的错误
     * @param msg 错误描述信息
     * @param tag 日志的TAG
     * @param toast 是否要用toast展示给用户
     */
    public static void displayError(final Activity activity, Exception e, final String msg,String tag, final boolean toast){
        if(toast){
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_SHORT).show();
            }
        }
        e.printStackTrace();
        Log.e(tag, msg );
    }
    static void displayError(final Activity activity, Exception e, final String toast_msg,String log_msg,String tag){
        if(activity!=null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,toast_msg,Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(MyApplication.getContext(),toast_msg,Toast.LENGTH_SHORT).show();
        }
        e.printStackTrace();
        Log.e(tag, log_msg );
    }
}
