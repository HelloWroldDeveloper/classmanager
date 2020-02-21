package com.chen.handle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.chen.activity.BaseActivity;
import com.chen.activity.MainActivity;
import com.chen.activity.RegisterActivity;
import com.chen.data.ClassSign;
import com.chen.data.Grade;
import com.chen.data.LeaveApplication;
import com.chen.data.MyDate;
import com.chen.data.Notice;
import com.chen.data.User;
import com.chen.data.VoteData;
import com.chen.data.VoteID;
import com.chen.data.VotedID;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//数据输出类
//用于向本地或网络保存数据
public class DataOutput {
    private static final String[] LEAVE_APPLICATION_KEYS=new String[]{LeaveApplication.KEY_TITLE,LeaveApplication.KEY_CONTENT
            ,LeaveApplication.KEY_START_DATE,LeaveApplication.KEY_START_TIME,
            LeaveApplication.KEY_END_DATE,LeaveApplication.KEY_END_TIME};//用于保存和读取用户创建的请假申请的关键字
    private static final String TAG="dataOutput";

    //保存当前用户的信息到本地
    private static void saveNowUser(BaseActivity activity){
        SharedPreferences.Editor editor=activity.getSharedPreferences("now_user",Context.MODE_PRIVATE).edit();
        User now=User.getNowUser();
        editor.putString(User.NAME,now.getName());
        editor.putString(User.NUMBER,now.getNumber());
        editor.apply();
    }

    //保存用户创建的请假申请到本地
    public static void saveNowApplication(Context context, String key,String data){
        SharedPreferences.Editor editor=context.getSharedPreferences("now_application",Context.MODE_PRIVATE).edit();
        for(String s:LEAVE_APPLICATION_KEYS){
            if(!s.equals(key)){
                String temp=DataInput.getNowApplication(context,s);
                if(temp!=null){
                    editor.putString(s,temp);
                }
            }else {
                editor.putString(key,data);
            }
        }
        editor.apply();
    }
    //清除用户创建的请假申请
    public static void deleteApplication(Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences("now_application",Context.MODE_PRIVATE).edit();
        for(String s:LEAVE_APPLICATION_KEYS){
            editor.putString(s,"");
        }
        editor.apply();
    }

    //保存用户创建的投票选项到本地
    public static void saveNowVote(VoteData voteData){
        List<String> contents=voteData.getVote_contents();
        String title=voteData.getTitle();
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<contents.size();i++){
            builder.append(contents.get(i));
            if(i!=contents.size()-1){
                builder.append("_");
            }
        }
        SharedPreferences.Editor editor=MyApplication.getContext().getSharedPreferences("now_vote_data",Context.MODE_PRIVATE).edit();
        editor.putString(VoteData.CONTENTS,builder.toString());
        editor.putString(VoteData.TITLE,title);
        editor.apply();
    }
    //清除用户创建的投票选项
    public static void deleteNowVote(BaseActivity activity){
        SharedPreferences.Editor editor=activity.getSharedPreferences("now_vote_data",Context.MODE_PRIVATE).edit();
        editor.putString(VoteData.CONTENTS,"");
        editor.putString(VoteData.TITLE,"");
        editor.apply();
    }
    //向本地添加用户创建的投票的id
    public static void addVoteID(int id){
        VoteID vote_id=new VoteID();
        vote_id.setId(id);
        vote_id.save();
    }
    //从本地删除用户创建的投票的id
    public static void deleteVoteID(int id){
        DataSupport.deleteAll(VoteID.class,"id = ?",""+id);
    }
    //向本地添加用户已投的投票的id
    public static void addVotedID(int id){
        VotedID voted_id=new VotedID();
        voted_id.setId(id);
        voted_id.save();
    }
    //从本地删除用户已投的投票的id
    public static void deleteVotedID(int id){
        DataSupport.deleteAll(VotedID.class,"id = ?",""+id);
    }

    //向本地保存通知
    public static void saveNotices(){
        List<Notice> notices=Notice.getNotices();
        if(!notices.isEmpty()){
            DataSupport.deleteAll(Notice.class);
            for(Notice n:notices){
                n.save();
            }
        }
    }

    //向本地保存请假申请
    public static void saveApplication(){
        List<LeaveApplication> applications=LeaveApplication.getApplications();
        if(!applications.isEmpty()){
            DataSupport.deleteAll(LeaveApplication.class);
            for(LeaveApplication application:applications){
                application.save();
            }
        }
    }

    //向本地保存成绩上传事件
    public static void saveGrades(){
        List<Grade> grades=Grade.getGrades();
        if(!grades.isEmpty()){
            DataSupport.deleteAll(Grade.class);
            for(Grade g:grades){
                g.save();
            }
        }
    }

    //向本地保存用户的签到状态
    public static void saveSignStatus(BaseActivity activity){
        ClassSign sign=ClassSign.getNow_sign();
        SharedPreferences.Editor editor=activity.getSharedPreferences("sign_status",Context.MODE_PRIVATE).edit();
        if(sign!=null){
            editor.putInt("status", sign.getStatus());
        }else{
            editor.putInt("status", ClassSign.NOT_SIGNED);
        }
        editor.apply();
    }

    //发送反馈信息到服务器
    public static void sendFeedback(String content, final BaseActivity activity, final EditText advice_text){
        MyDate send_date=MyDate.getCurrentDate();
        //根据content和send_date给服务器发送反馈信息
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","send_feedback"));
        args.add(new HttpUtil.Arg("number",User.getNowUser().getNumber()));
        args.add(new HttpUtil.Arg("content",content));
        args.add(new HttpUtil.Arg("send_date",send_date.toString()));
        HttpUtil.sendPostHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"发送反馈时发生错误",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String status=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("status");
                    if(status.equals("ok")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"发送反馈成功",Toast.LENGTH_SHORT).show();
                                advice_text.setText("");
                            }
                        });
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"发送反馈失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"发送反馈时发生错误",TAG,true);
                }
            }
        });
    }

    //向服务器发起注册的请求(尚未验证邮箱)
    public static void user_register(final String number, String email, final String password){
        //接收到响应后记得跳转到RegisterActivity
        final String em=email;
        email=email.replace("@","_");
        email=email.replace(".","_");
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","user_register"));
        args.add(new HttpUtil.Arg("number",number));
        args.add(new HttpUtil.Arg("email",email));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(null,e,"注册失败","用户发起注册(尚未验证邮箱)失败",TAG);
            }

            @Override
            public void onResponse(Call call, Response response){
                try{
                    String result= URLDecoder.decode(response.body().string(),"utf-8");
                    JSONObject jsonObject=new JSONObject(result);
                    String r=jsonObject.getString("status");
                    if(r.equals("ok")){
                        RegisterActivity.actionStart(number,em,password,MyApplication.getContext());
                    }else {
                        try{
                            Toast.makeText(MyApplication.getContext(),"注册失败",Toast.LENGTH_SHORT).show();
                        }catch (Exception e1){
                            Looper.prepare();
                            Toast.makeText(MyApplication.getContext(),"注册失败",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        Log.e(TAG, "用户发起注册(尚未验证邮箱)失败" );
                    }
                }catch (Exception e){
                    Util.displayError(null,e,"注册失败","用户发起注册(尚未验证邮箱)失败",TAG);
                }
            }
        });
    }
    //向服务器发起注册的请求(等待验证邮箱)
    public static void user_activate(final String number, final String name, String password, String email, String activate_code, final BaseActivity activity){
        //接收到成功的响应后记得调用User.setNow_user(new User(number,name,DataInput.getUserType()))然后跳转到MainActivity
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","user_register_activate"));
        args.add(new HttpUtil.Arg("number",number));
        args.add(new HttpUtil.Arg("name",name));
        args.add(new HttpUtil.Arg("password",password));
        args.add(new HttpUtil.Arg("email",email));
        args.add(new HttpUtil.Arg("is_admin",DataInput.getUserType()+""));
        args.add(new HttpUtil.Arg("activate_code",activate_code));
        HttpUtil.sendPostHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(null,e,"激活失败(发生未知错误)","用户发起注册(尚未验证邮箱)失败",TAG);
            }

            @Override
            public void onResponse(Call call, Response response){
                try{
                    String result= URLDecoder.decode(response.body().string(),"utf-8");
                    JSONObject jsonObject=new JSONObject(result);
                    final String r=jsonObject.getString("status");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (r){
                                case "ok":
                                    User.setNow_user(new User(number,name,DataInput.getUserType()));
                                    DataOutput.saveNowUser(activity);
                                    Intent intent=new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                    List<Activity> activities=ActivityCollector.getActivities();
                                    for(int i=0;i<activities.size()-1;i++){
                                        activities.get(i).finish();
                                    }
                                    break;
                                case "overtime":
                                    Toast.makeText(activity,"激活码已过期",Toast.LENGTH_SHORT).show();
                                    activity.finish();
                                    break;
                                case "error":
                                    Toast.makeText(activity,"激活码错误",Toast.LENGTH_SHORT).show();
                                    break;
                                case "number_already":
                                    Toast.makeText(activity,"学号已注册",Toast.LENGTH_SHORT).show();
                                    activity.finish();
                                    break;
                                case "email_already":
                                    Toast.makeText(activity,"邮箱已注册",Toast.LENGTH_SHORT).show();
                                    activity.finish();
                                    break;
                                default:
                                    Toast.makeText(activity,"激活失败(发生未知错误)",Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "用户发起注册(等待验证邮箱)失败" );
                                    break;
                            }
                        }
                    });
                }catch (Exception e){
                    Util.displayError(null,e,"激活失败(发生未知错误)","用户发起注册(等待验证邮箱)失败",TAG);
                }
            }
        });
    }
}
