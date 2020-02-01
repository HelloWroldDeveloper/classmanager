package com.chen.data;

import java.util.ArrayList;
import java.util.List;

//列表布局风格 实体类
public class MemberStyle {
    public static final int STYLE_USER_COMMON=1;//表示使用"普通用户"布局
    public static final int STYLE_USER_ME=2;//表示使用"当前用户"布局
    public static final int STYLE_TIP_COMMON_USER=3;//表示使用"普通用户提示"布局
    public static final int STYLE_TIP_ADMIN_USER=4;//表示使用"管理员提示"布局
    public static final int STYLE_WRITE=5;//表示使用"空白"布局

    private int type;//布局类型
    private User user=null;

    public MemberStyle(int type,User user){
        this.type=type;
        this.user=user;
    }
    public MemberStyle(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }
    public User getUser() {
        return user;
    }
    public static List<MemberStyle> generateMemberStyles(){
        //根据用户列表生成一个用于展示的适配器的列表
        List<MemberStyle> styles=new ArrayList<>();
        List<User> common_user=User.getLatestCommonUsers();
        List<User> admins=User.getLatestAdminUsers();
        styles.add(new MemberStyle(MemberStyle.STYLE_TIP_COMMON_USER));
        for(User u:common_user){
            if(!u.isNowUser()){
                styles.add(new MemberStyle(MemberStyle.STYLE_USER_COMMON,u));
            }else{
                styles.add(new MemberStyle(MemberStyle.STYLE_USER_ME,u));
            }
        }
        styles.add(new MemberStyle(MemberStyle.STYLE_WRITE));
        styles.add(new MemberStyle(MemberStyle.STYLE_TIP_ADMIN_USER));
        for(User u:admins){
            if(!u.isNowUser()){
                styles.add(new MemberStyle(MemberStyle.STYLE_USER_COMMON,u));
            }else{
                styles.add(new MemberStyle(MemberStyle.STYLE_USER_ME,u));
            }
        }
        return styles;
    }
}
