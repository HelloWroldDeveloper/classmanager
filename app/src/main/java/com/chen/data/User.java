package com.chen.data;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.chen.activity.BaseActivity;
import com.chen.handle.DataInput;
import com.chen.handle.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//用户 实体类
public class User {
    private static final String TAG="user";
    //用于保存当前用户信息的key
    public static final String NUMBER="number";
    public static final String NAME="name";
    //用于保存当前用户信息的key


    private static List<User> class_users=new ArrayList<>();//班级成员列表
    private static List<User> common_users=new ArrayList<>();//普通用户列表
    private static List<User> admin_users=new ArrayList<>();//管理员列表

    private static User now_user;

    private String number;
    private String name;
    private boolean isAdmin;

    public User(String number,String name,boolean isAdmin){
        this.number=number;
        this.name=name;
        this.isAdmin=isAdmin;
    }

    public String getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public static List<User> getClass_users(){
        return class_users;
    }
    static List<User> getCommonUsers(){
        return common_users;
    }
    static List<User> getAdminUsers(){
        return admin_users;
    }
    public static User getNowUser(){
        return now_user;
    }
    public static void setNow_user(User user){
        now_user=user;
    }

    public static void initUsers(){
        //初始化班级用户列表
        updateUsers(null,null,null);
        /*
        //仅供测试使用
        User monitor=new User("000000","monitor",true);
        class_users.add(monitor);
        for(int i=1;i<=49;i++){
            String number="";
            for(int j=1;j<=3;j++){
                number+=(""+i);
            }
            User user=new User(number,i+""+i,false);
            if(i==1){
                user.setNumber("2019040705010");
                setNow_user(user);
            }
            class_users.add(user);
        }
        //仅供测试使用
         */
    }
    public static void updateUsers(final TextView members_num, final BaseActivity activity, final Button info){
        //更新班级用户列表
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_user_list"));
        args.add(new HttpUtil.Arg("class", DataInput.getClassNumber()));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateError(e,activity,info);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                class_users=new ArrayList<>();
                String result= URLDecoder.decode(response.body().string(),"utf-8");
                try {
                    JSONArray array=new JSONArray(result);
                    int n=array.length();
                    for(int i=0;i<n;i++){
                        JSONObject object=array.getJSONObject(i);
                        String number=object.getString("number");
                        String name=object.getString("name");
                        String is_admin=object.getString("is_admin");
                        boolean admin=is_admin.equals("1");
                        class_users.add(new User(number,name,admin));
                    }
                    common_users=new ArrayList<>();
                    admin_users=new ArrayList<>();
                    for(User user:class_users){
                        if(user.isAdmin()){
                            admin_users.add(user);
                        }else {
                            common_users.add(user);
                        }
                    }
                    if(activity!=null){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                members_num.setText(""+class_users.size());
                                info.setEnabled(true);
                                info.setText("查看");
                            }
                        });
                    }
                }catch (Exception e){
                    updateError(e,activity,info);
                }
            }
        });
    }
    //更新用户列表时出错的提示
    private static void updateError(Exception e,BaseActivity activity,final Button info){
        e.printStackTrace();
        Log.e(TAG, "获取班级成员列表失败" );
        if(activity!=null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(info!=null){
                        info.setEnabled(false);
                        info.setText("错误");
                    }
                }
            });
        }
    }
}
