package com.chen.data;

import android.util.Log;
import android.widget.Toast;

import com.chen.activity.BaseActivity;
import com.chen.adapter.LeaveApplyContentAdapter;
import com.chen.fragment.Leave_apply_add_s3_fragment;
import com.chen.handle.DataInput;
import com.chen.handle.DataOutput;
import com.chen.handle.HttpUtil;
import com.chen.handle.Util;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//请假申请 实体类
public class LeaveApplication extends DataSupport {
    public static final int REVIEWING=1;//状态:审核中
    public static final int PASSED=2;//状态:已通过
    public static final int NO_PASS=3;//状态:未通过
    public static final int IGNORED=4;//状态:已忽略
    public static final int OVER_DUE=5;//状态:已过期
    public static final String TAG="LeaveApplication";//用于生成日志的tag

    //用于保存和读取数据的关键字
    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_START_DATE="start_date";
    public static final String KEY_END_DATE="end_date";
    public static final String KEY_START_TIME="start_time";
    public static final String KEY_END_TIME="end_time";
    //用于保存和读取数据的关键字

    private static List<LeaveApplication> applications;//请假申请 列表

    private String title;
    private String content;
    private String start_date;
    private String end_date;
    private String create_date;
    private int status;
    private int application_id;

    public LeaveApplication(String title,String content,MyDate start_date,MyDate end_date,int id){
        this.title=title;
        this.content=content;
        this.start_date=start_date.toString();
        this.end_date=end_date.toString();
        this.status=LeaveApplication.REVIEWING;
        this.create_date=MyDate.getCurrentDate().toString();
        this.application_id =id;
    }

    public String getTitle(){
        return  title;
    }
    public String getContent() {
        return content;
    }
    public MyDate getStart_date() {
        return MyDate.parseMyDate(start_date);
    }
    public MyDate getEnd_date() {
        return MyDate.parseMyDate(end_date);
    }
    public MyDate getCreate_date() {
        return MyDate.parseMyDate(create_date);
    }
    public int getStatus() {
        return status;
    }
    public int getApplication_id() {
        return application_id;
    }
    public String getStatusStatement(){
        switch (status){
            case LeaveApplication.REVIEWING:
                return "审核中";
            case LeaveApplication.PASSED:
                return "已通过";
            case LeaveApplication.NO_PASS:
                return "未通过";
            case LeaveApplication.IGNORED:
                return "已忽略";
            case LeaveApplication.OVER_DUE:
                return "已过期";
            default:
                return null;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //更新某个请假申请的状态
    public void updateStatus(final LeaveApplyContentAdapter adapter, final BaseActivity activity){
        //根据id向服务器发起查询请假申请状态的请求，并保存到result中
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_application_status"));
        args.add(new HttpUtil.Arg("id",getApplication_id()+""));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"查询请假申请状态失败",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String result=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("status");
                    switch (result){
                        case "no":
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity,"在服务器找不到对应的请假申请",Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        case "1":
                            int status=getStatus();
                            if((status!=LeaveApplication.OVER_DUE)&&MyDate.before_now(getEnd_date())){
                                setStatus(LeaveApplication.OVER_DUE);
                            }else if((status==LeaveApplication.REVIEWING)&&MyDate.before_now(getStart_date())){
                                setStatus(LeaveApplication.IGNORED);
                            }
                            break;
                        case "2":
                            setStatus(LeaveApplication.NO_PASS);
                            break;
                        case "3":
                            setStatus(LeaveApplication.PASSED);
                            break;
                    }
                    adapter.setApplication(LeaveApplication.this);
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Util.displayError(activity,e,"查询请假申请状态失败",TAG,true);
                }
            }
        });
    }

    //初始化请假申请列表
    public static void initApplications(){

        applications= DataInput.getApplications();
        /*
        //测试
        MyDate date1=new MyDate(2020,1,1,1,1);
        MyDate date2=new MyDate(2020,1,2,2,2);
        for (int i=1;i<=50;i++){
            String content="";
            for (int k=1;k<=600;k++){
                content+=(""+i);
            }
            LeaveApplication leave=new LeaveApplication(i+""+i,content,date1,date2,i);
            applications.add(leave);
        }
        //测试
        */
    }
    public static List<LeaveApplication> getApplications(){
        return applications;
    }
    //通过id查找 请假申请
    public static LeaveApplication findApplicationByID(int id){
        for(LeaveApplication application:applications){
            if(application.getApplication_id()==id){
                return application;
            }
        }
        return null;
    }

    //添加新的请假申请
    public static void addNewApplication(final String title, final String content, final MyDate start_date, final MyDate end_date,
                                         final Leave_apply_add_s3_fragment fragment, final BaseActivity activity){
        //根据title,content,start_date,end_date和create_date向服务器发出创建请假申请的请求
        User now_user=User.getNowUser();
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","add_application"));
        args.add(new HttpUtil.Arg("number",now_user.getNumber()));
        args.add(new HttpUtil.Arg("name",now_user.getName()));
        args.add(new HttpUtil.Arg("class",DataInput.getClassNumber()));
        args.add(new HttpUtil.Arg("title",title));
        args.add(new HttpUtil.Arg("content",content));
        args.add(new HttpUtil.Arg("start_date",start_date.toString()));
        args.add(new HttpUtil.Arg("end_date",end_date.toString()));
        args.add(new HttpUtil.Arg("create_date",MyDate.getCurrentDate().toString()));
        HttpUtil.sendPostHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"创建请假申请失败",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    int id=Integer.parseInt(new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("id"));
                    applications.add(new LeaveApplication(title,content,start_date,end_date,id));
                    Toast.makeText(activity,"成功创建申请",Toast.LENGTH_SHORT).show();
                    fragment.setCreating(true);
                    DataOutput.deleteApplication(activity);
                    activity.finish();
                }catch (Exception e){
                    Util.displayError(activity,e,"创建请假申请失败",TAG,true);
                }
            }
        });
    }

    public static void deleteApplication(int id,final BaseActivity activity){
        for(int i=0;i<applications.size();i++){
            if(applications.get(i).getApplication_id()==id){
                applications.remove(i);
                break;
            }
        }
        //根据id向服务器发出删除请假申请的请求(在此书写逻辑)
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","delete_application"));
        args.add(new HttpUtil.Arg("id",id+""));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"删除请假申请失败",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String status=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("status");
                    if(status.equals("ok")){
                        Toast.makeText(activity,"删除成功",Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"在服务器找不到相应的请假申请",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"删除请假申请失败",TAG,true);
                }
            }
        });
    }
    //更新请假申请列表
    public static void updateApplications(){
        //根据开始时间和结束日期更新请假申请
        try{
            for(LeaveApplication application:applications){
                if((application.getStatus()!=LeaveApplication.OVER_DUE)&&MyDate.before_now(application.getEnd_date())){
                    application.setStatus(LeaveApplication.OVER_DUE);
                }
            }
        }catch (ParseException e){
            e.printStackTrace();
            Log.e(TAG,"无法解析日期");
        }
        //根据开始时间和结束日期更新请假申请
    }
}
