package com.chen.data;


import android.widget.Toast;

import com.chen.activity.BaseActivity;
import com.chen.adapter.SubmitGradeAdapter;
import com.chen.handle.DataInput;
import com.chen.handle.HttpUtil;
import com.chen.handle.Util;

import org.json.JSONArray;
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

//成绩上传 实体类
public class Grade extends DataSupport {
    public static final int SUBMITTED=1;//状态: 已提交
    public static final int NOT_SUBMITTED=2;//状态: 未提交
    public static final int OVER_DUE=3;//状态: 已过期
    public static final String TAG="Grade";//用于生成日志的tag

    private static List<Grade> grades;//成绩上传 列表
    private static boolean allow_activity_update=true;//是否允许对应的活动更新成绩上传列表

    private String test_name;//考试名称
    private String monitor;//统计人
    private int nowScore=-1;//用户填写的成绩
    private int status=NOT_SUBMITTED;//状态
    private int grade_id;//索引
    private String date;//发送日期
    private String deadline;//截止日期

    private Grade(String test_name,String monitor,MyDate date,MyDate deadline,int id){
        this.test_name=test_name;
        this.monitor=monitor;
        this.date=date.toString();
        this.deadline=deadline.toString();
        this.grade_id =id;
    }

    public int getGrade_id() {
        return grade_id;
    }
    public int getStatus() {
        return status;
    }
    public int getNowScore() {
        return nowScore;
    }
    public String getExtraMsg() {
        return monitor+" "+date;
    }
    public String getTest_name() {
        return test_name;
    }
    public String getMonitor() {
        return monitor;
    }
    public String getDeadline_msg() {
        return deadline;
    }

    public void setNowScore(int nowScore) {
        this.nowScore = nowScore;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public boolean is_overdue() throws ParseException {
        return MyDate.before_now(MyDate.parseMyDate(deadline));
    }
    //通过id获取成绩上传事件
    public static Grade findGradeByID(int id){
        int a=0,b=grades.size()-1;
        while(true){
            Grade A=grades.get(a),
                    B=grades.get(b);
            if(A.getGrade_id()==id){
                return A;
            }else if(B.getGrade_id()==id){
                return B;
            }else if(A.getGrade_id()>id){
                break;
            }else if(B.getGrade_id()<id){
                break;
            }
            if (a-b==1||a-b==-1) {
                break;
            }
            int k=(a+b)/ 2;
            if (grades.get(k).getGrade_id() > id) {
                b=k;
            }else{
                a=k;
            }
        }
        return null;
    }

    public static boolean isAllow_activity_update() {
        return allow_activity_update;
    }
    public static void setAllow_activity_update(boolean allow_activity_update) {
        Grade.allow_activity_update = allow_activity_update;
    }

    public static void initGrades(){
        //初始化成绩列表
        grades= DataInput.getGrades();
        updateGrades(null,null);
        /*
        //测试
        grades.add(new Grade("000000","陈家耀",new MyDate(1,1,1,1,1),new MyDate(2020,2,28,18,10),1));
        for(int i=1;i<=15;i++){
            String test_name="";
            for(int j=1;j<=10;j++)
                test_name+=(""+i);
            Grade grade=new Grade(test_name,"陈家耀",new MyDate(i,i,i,i,i),new MyDate(2020,1,27,18,10),i+1);
            grades.add(grade);
        }
        //测试
        */
    }
    public static List<Grade> getGrades(){
        return grades;
    }
    //更新成绩上传事件列表
    public static void updateGrades(final BaseActivity activity, final SubmitGradeAdapter adapter){
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_grade_submit"));
        args.add(new HttpUtil.Arg("class",DataInput.getClassNumber()));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"获取成绩上传列表失败",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    List<Grade> new_grades=new ArrayList<>();
                    //从服务器获取成绩上传事件列表并保存到new_grades中
                    String result= URLDecoder.decode(response.body().string(),"utf-8");
                    JSONArray array=new JSONArray(result);
                    String status=array.getJSONObject(0).getString("status");
                    if(status.equals("ok")){
                        int n=array.length();
                        for(int i=1;i<n;i++){
                            JSONObject object=array.getJSONObject(i);
                            String test_name=object.getString("test_name");
                            String initiator=object.getString("initiator");
                            MyDate send_date=MyDate.parseMyDate(object.getString("send_date"));
                            MyDate deadline=MyDate.parseMyDate(object.getString("deadline"));
                            int id=Integer.parseInt(object.getString("id"));
                            Grade g=new Grade(test_name,initiator,send_date,deadline,id);
                            new_grades.add(g);
                        }
                    }else{
                        grades=new ArrayList<>();
                        if(activity!=null){
                            Util.recreateActivity(activity);
                        }
                        return;
                    }
                    //从服务器获取成绩上传事件列表并保存到new_grades中
                    //新旧列表进行比对以同步数据
                    if(!new_grades.isEmpty()){
                        if(!grades.isEmpty()){
                            //整理出已经输入了成绩的成绩上传事件
                            List<Grade> old_submitted_grades=new ArrayList<>();
                            for(Grade g:grades){
                                if(g.getNowScore()!=-1){
                                    old_submitted_grades.add(g);
                                }
                            }
                            //整理出已经输入了成绩的成绩上传事件
                            //使用二分查找 从新列表匹配已经输入了成绩的成绩上传事件
                            int a=0,b;
                            boolean flag=true;
                            for(int i=0;i<old_submitted_grades.size()&&flag;i++){
                                Grade now=old_submitted_grades.get(i);
                                int now_search_id=now.getGrade_id();
                                b=new_grades.size()-1;
                                while(true){
                                    Grade A=new_grades.get(a),
                                            B=new_grades.get(b);
                                    if(A.getGrade_id()==now_search_id){
                                        A.setNowScore(now.getNowScore());
                                        break;
                                    }else if(B.getGrade_id()==now_search_id){
                                        B.setNowScore(now.getNowScore());
                                        break;
                                    }else if(A.getGrade_id()>now_search_id){
                                        break;
                                    }else if(B.getGrade_id()<now_search_id){
                                        flag=false;break;
                                    }
                                    if (a-b==1||a-b==-1) {
                                        break;
                                    }
                                    int k=(a+b)/ 2;
                                    if (new_grades.get(k).getGrade_id() > now_search_id) {
                                        b=k;
                                    }else{
                                        a=k;
                                    }
                                }
                            }
                            //使用二分查找
                            grades=new_grades;
                        }else {
                            grades=new_grades;
                        }
                    }
                    //新旧列表进行比对以同步数据
                    //更新成绩上传事件的状态
                    for(Grade g:grades){
                        boolean overdue=g.is_overdue();
                        if((g.getStatus()!=Grade.OVER_DUE)&&overdue){
                            g.setStatus(Grade.OVER_DUE);
                        }else if((g.getStatus()!=Grade.OVER_DUE)&&(!overdue)&&g.getNowScore()!=-1){
                            g.setStatus(Grade.SUBMITTED);
                        }
                    }
                    if(activity!=null){
                        if(adapter!=null){
                            adapter.setGrades(grades);
                            adapter.notifyDataSetChanged();
                        }else {
                            Util.recreateActivity(activity);
                        }
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"获取成绩上传列表失败",TAG,true);
                }
            }
        });
    }
    //用户上传自己的成绩
    public static void submit_grade(final Grade g,final BaseActivity activity){
        //根据id和score向服务器发起上传成绩的请求
        final int id=g.getGrade_id();
        final int score=g.getNowScore();
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","send_grade"));
        args.add(new HttpUtil.Arg("number",User.getNowUser().getNumber()));
        args.add(new HttpUtil.Arg("class",DataInput.getClassNumber()));
        args.add(new HttpUtil.Arg("test_id",id+""));
        args.add(new HttpUtil.Arg("score",score+""));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"上传成绩失败",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response){
                try{
                    String status=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("status");
                    if(status.equals("ok")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"上传成功",Toast.LENGTH_SHORT).show();
                                if(g.getStatus()==Grade.NOT_SUBMITTED){
                                    //用户上传成绩
                                    g.setStatus(Grade.SUBMITTED);//把当前成绩上传项目的状态设置为"已提交"
                                    Toast.makeText(activity,"上传成功",Toast.LENGTH_SHORT).show();
                                }else{
                                    //用户修改成绩
                                    Toast.makeText(activity,"修改成功",Toast.LENGTH_SHORT).show();
                                }
                                activity.finish();
                            }
                        });
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"错误:该成绩上传事件不存在",Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        });
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"上传成绩失败",TAG,true);
                }
            }
        });
    }
}
