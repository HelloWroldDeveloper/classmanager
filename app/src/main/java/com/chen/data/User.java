package com.chen.data;

import java.util.ArrayList;
import java.util.List;

//用户 实体类
public class User {
    private static List<User> class_users=new ArrayList<>();

    private static User now_user;

    private String number;
    private String name;
    private boolean isAdmin=false;
    private boolean isNowUser=false;

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
    public boolean isNowUser(){
        return isNowUser;
    }
    public void setAuthority(boolean isAdmin){
        this.isAdmin=isAdmin;
    }
    public void setNow_user(){
        this.isNowUser=true;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public static void initUsers(){
        //初始化班级用户列表(在此书写逻辑)
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
                now_user=user;
                user.setNow_user();
            }
            class_users.add(user);
        }
        //初始化班级用户列表(在此书写逻辑)
    }
    public static List<User> getLatestUsers(){
        //获取最新的班级用户列表(在此书写逻辑)
        return class_users;
        //获取最新的班级用户列表(在此书写逻辑)
    }
    public static List<User> getLatestCommonUsers(){
        getLatestUsers();
        List<User> class_common_users=new ArrayList<>();
        for(User user:class_users){
            if(!user.isAdmin()){
                class_common_users.add(user);
            }
        }
        return class_common_users;
    }
    public static List<User> getLatestAdminUsers(){
        getLatestUsers();
        List<User> admin_users=new ArrayList<>();
        for(User user:class_users){
            if(user.isAdmin()){
                admin_users.add(user);
            }
        }
        return admin_users;
    }
    public static User getNowUser(){
        //获取当前用户(在此书写逻辑)
        return now_user;
        //获取当前用户(在此书写逻辑)
    }

}
