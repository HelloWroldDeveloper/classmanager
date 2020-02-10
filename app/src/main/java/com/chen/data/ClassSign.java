package com.chen.data;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.chen.activity.BaseActivity;
import com.chen.activity.R;
import com.chen.handle.HttpUtil;
import com.chen.handle.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ClassSign {
    public static class StudentSign{
        private String name;
        private boolean signed;
        private StudentSign(String name,boolean signed){
            this.name=name;
            this.signed=signed;
        }
        public String getName() {
            return name;
        }
        private boolean isSigned() {
            return signed;
        }
    }

    public static final String TAG="ClassSign";//用于生成日志的tag
    public static final int NOT_SIGNED=1;//状态：未签到
    public static final int SIGNED=2;//状态：签到成功
    public static final int FAILED=3;//状态：签到失败

    private static ClassSign now_sign=null;//当前的签到
    private static boolean allow_activity_update=true;//是否允许对应的活动更新当前签到

    private String place;//签到地点
    private String initiator;//发起人
    private double[] center;//签到中心(用于核验用户是否签到成功) 第一个是纬度，第二个是经度
    private int distance;//允许的最大签到距离半径
    private MyDate deadline;//签到的截止时间
    private int status;//签到状态
    private int id;//签到在服务器sql中的id
    private List<StudentSign> studentSigns;//签到人员列表

    private ClassSign(String place,String initiator,double[] center,int distance,MyDate deadline,int id,List<StudentSign> signs){
        this.place=place;
        this.initiator=initiator;
        this.center=center;
        this.distance=distance;
        this.deadline=deadline;
        this.status=NOT_SIGNED;
        this.id=id;
        this.studentSigns=signs;
    }

    public String getPlace() {
        return place;
    }
    public String getInitiator() {
        return initiator;
    }
    public double[] getCenter() {
        return center;
    }
    public int getDistance() {
        return distance;
    }
    public MyDate getDeadline() {
        return deadline;
    }
    public int getStatus() {
        return status;
    }
    public int getId() {
        return id;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public static boolean isAllow_activity_update() {
        return allow_activity_update;
    }
    public static void setAllow_activity_update(boolean allow_activity_update) {
        ClassSign.allow_activity_update = allow_activity_update;
    }

    public static ClassSign getNow_sign() {
        return now_sign;
    }

    //从服务器查询对应于当前用户的签到
    public static void searchClassSign(final BaseActivity activity){
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_sign_events"));
        args.add(new HttpUtil.Arg("number",User.getNowUser().getNumber()));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"查询签到失败",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject object=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8"));
                    if(object.getString("status").equals("ok")){
                        int id=Integer.parseInt(object.getString("id"));
                        findSign(id,activity);
                    }else{
                        now_sign=null;
                        Util.recreateActivity(activity);
                    }
                }catch (JSONException e){
                    Util.displayError(activity,e,"查询签到失败",TAG,true);
                }
            }
        });
    }
    //根据id从服务器获取签到
    private static void findSign(int id,final BaseActivity activity){
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_sign"));
        args.add(new HttpUtil.Arg("id_list",""+id));
        HttpUtil.sendPostHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"寻找签到时出错",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONArray array=new JSONArray(URLDecoder.decode(response.body().string(),"utf-8"));
                    JSONObject object=array.getJSONObject(0);
                    String status=object.getString("status");
                    if(status.equals("ok")){
                        JSONArray msg=array.getJSONArray(1);
                        JSONObject sign=msg.getJSONObject(0);
                        int n=msg.length();
                        String place=sign.getString("place");
                        String initiator=sign.getString("initiator");
                        String center_w=sign.getString("center_w");
                        String center_j=sign.getString("center_j");
                        String distance=sign.getString("distance");
                        String deadline=sign.getString("deadline");
                        String sign_id=null;
                        List<StudentSign> studentSigns=new ArrayList<>();
                        for(int i=1;i<n;i++){
                            JSONObject student=msg.getJSONObject(i);
                            if(sign_id==null){
                                sign_id=student.getString("sign_id");
                            }
                            String name=student.getString("name");
                            String sign_status=student.getString("sign_status");
                            studentSigns.add(new StudentSign(name,sign_status.equals("3")));
                        }
                        double[] center=new double[2];
                        center[0]=Double.parseDouble(center_w);
                        center[1]=Double.parseDouble(center_j);
                        now_sign=new ClassSign(place,initiator,center,Integer.parseInt(distance),MyDate.parseMyDate(deadline),Integer.parseInt(sign_id),studentSigns);
                        Util.recreateActivity(activity);
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"找不到对应的签到",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "找不到对应的签到");
                            }
                        });
                    }
                }catch (JSONException e){
                    Util.displayError(activity,e,"寻找签到时出错",TAG,true);
                }
            }
        });
    }

    public void sign(final int distance,boolean succeed, final Button sign_btn, final BaseActivity activity){
        sign_btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn_grey));
        sign_btn.setEnabled(false);
        sign_btn.setText("请稍候");
        if(succeed){
            this.status=ClassSign.SIGNED;
        }else {
            this.status=ClassSign.FAILED;
        }
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","execute_sign"));
        args.add(new HttpUtil.Arg("number",User.getNowUser().getNumber()));
        args.add(new HttpUtil.Arg("sign_status",succeed ? "3":"2"));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"签到时发生错误",TAG,true);
                displayError(activity,sign_btn);
            }

            @Override
            public void onResponse(Call call, Response response){
                try{
                    String status=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("status");
                    if(status.equals("ok")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sign_btn.setEnabled(false);
                                sign_btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn_green));
                                sign_btn.setText("签到成功");
                                Toast.makeText(activity,"签到成功 距离为"+distance+"米",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sign_btn.setEnabled(false);
                                sign_btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn_red));
                                sign_btn.setText("签到失败");
                                Toast.makeText(activity,"签到失败 距离为"+distance+"米",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"签到时发生错误",TAG,true);
                    displayError(activity,sign_btn);
                }
            }
        });
    }
    //签到时发生错误时的提示
    private static void displayError(BaseActivity activity,final Button sign_btn){
        try{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sign_btn.setText("错误");
                }
            });
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }
    public List<ClassSign.StudentSign> getSignedStudents(){
        List<StudentSign> signs=new ArrayList<>();
        for(StudentSign s:studentSigns){
            if(s.isSigned()){
                signs.add(s);
            }
        }
        return signs;
    }
    public List<ClassSign.StudentSign> getUnsignedStudents(){
        List<StudentSign> signs=new ArrayList<>();
        for(StudentSign s:studentSigns){
            if(!s.isSigned()){
                signs.add(s);
            }
        }
        return signs;
    }
}
