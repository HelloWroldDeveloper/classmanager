package com.chen.handle;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.chen.activity.BaseActivity;
import com.chen.data.AppVersion;
import com.chen.data.ClassSign;
import com.chen.data.Grade;
import com.chen.data.LeaveApplication;
import com.chen.data.Notice;
import com.chen.data.User;
import com.chen.data.VoteData;
import com.chen.data.VoteID;
import com.chen.data.VotedID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//数据输入类
//用于从本地或网络获取数据
public class DataInput {
    private final static String version="1.00";
    private final static boolean user_type=false;//当前用户是否管理员
    private final static String TAG="dataInput";

    public static String getNowVersion(){
        //获取app的版本号
        return version;
    }
    static boolean getUserType(){
        //获取用户类型
        return user_type;
    }

    public static String getClassNumber() {
        //获得班级编号
        return User.getNowUser().getNumber().substring(0, 10);
    }

    //从本地获取当前用户的信息
    public static void updateNowUser(BaseActivity activity){
        SharedPreferences pref=activity.getSharedPreferences("now_user",Context.MODE_PRIVATE);
        User.setNow_user(new User(pref.getString(User.NUMBER,""),pref.getString(User.NAME,""),getUserType()));
    }

    //从本地获取用户创建的请假申请
    public static String getNowApplication(Context context,String key){
        String result;
        SharedPreferences pref=context.getSharedPreferences("now_application",Context.MODE_PRIVATE);
        result=pref.getString(key,null);
        if (result!=null&&result.equals("")){
            result=null;
        }
        return result;
    }

    //从本地获得用户创建的投票选项
    public static VoteData getNowVoteData(){
        List<String> vote_contents=new ArrayList<>();
        String title;
        String content_str;
        SharedPreferences pref=MyApplication.getContext().getSharedPreferences("now_vote_data",Context.MODE_PRIVATE);
        content_str=pref.getString(VoteData.CONTENTS,"");
        title=pref.getString(VoteData.TITLE,"");
        String[] contents=null;
        if(!content_str.equals("")){
            contents=content_str.split("_");
        }
        if(contents!=null){
            vote_contents.addAll(Arrays.asList(contents));
        }
        return new VoteData(vote_contents,title);
    }
    //从本地获取用户创建的投票的id
    public static List<Integer> getVoteIDs(){
        List<Integer> ids=new ArrayList<>();
        List<VoteID> voteIDS= DataSupport.order("vote_id asc").find(VoteID.class);
        if(voteIDS!=null){
            for(VoteID id:voteIDS){
                ids.add(id.getId());
            }
        }
        return ids;
    }
    //从本地获取用户已投的投票的id
    public static List<Integer> getVotedIDs(){
        List<Integer> ids=new ArrayList<>();
        List<VotedID> votedIDS= DataSupport.order("voted_id asc").find(VotedID.class);
        if(votedIDS!=null){
            for(VotedID id:votedIDS){
                ids.add(id.getId());
            }
        }
        return ids;
    }

    //从本地读取通知
    public static List<Notice> getNotices(){
        List<Notice> notices;
        notices=DataSupport.order("notice_id asc").find(Notice.class);
        if(notices==null){
            notices=new ArrayList<>();
        }
        return notices;
    }

    //从本地读取请假申请
    public static List<LeaveApplication> getApplications(){
        List<LeaveApplication> applications;
        applications=DataSupport.order("application_id asc").find(LeaveApplication.class);
        if(applications==null){
            applications=new ArrayList<>();
        }
        return applications;
    }

    //从本地获取成绩上传事件
    public static List<Grade> getGrades(){
        List<Grade> grades;
        grades=DataSupport.order("grade_id asc").find(Grade.class);
        if(grades==null){
            grades=new ArrayList<>();
        }
        return grades;
    }

    //从本地获取用户的签到状态
    public static int getSignStatus(BaseActivity activity){
        SharedPreferences pref=activity.getSharedPreferences("sign_status",Context.MODE_PRIVATE);
        return pref.getInt("status", ClassSign.NOT_SIGNED);
    }

    //从服务器获取更新日志
    public static void checkUpdate(final BaseActivity activity){
        //从服务器获取客户端信息

        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_app_msg"));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"无法获得客户端信息", TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    JSONArray array=new JSONArray(URLDecoder.decode(response.body().string(),"utf-8"));
                    int n=array.length();
                    List<AppVersion> versions=new ArrayList<>();
                    for(int i=0;i<n;i++){
                        JSONObject object=array.getJSONObject(i);
                        String version=object.getString("version");
                        String update=object.getString("update");
                        String update_date=object.getString("update_date");
                        AppVersion v=new AppVersion(version,update,update_date);
                        versions.add(v);
                    }
                    final List<AppVersion> new_versions=new ArrayList<>();
                    boolean flag=false;
                    for(AppVersion version:versions){
                        if(flag||Double.parseDouble(version.getVersion())>Double.parseDouble(DataInput.getNowVersion())){
                            flag=true;
                            new_versions.add(version);
                        }
                    }
                    //根据new_versions显示更新日志或进行更新
                    if(!new_versions.isEmpty()){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"最新版本:"+new_versions.get(new_versions.size()-1).getVersion(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"暂无更新的版本",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"无法获得客户端信息", TAG,true);
                }
            }
        });
    }
}
