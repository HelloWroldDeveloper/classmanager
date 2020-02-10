package com.chen.data;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.chen.activity.BaseActivity;
import com.chen.activity.R;
import com.chen.adapter.NoticeAdapter;
import com.chen.fragment.Notice_fragment;
import com.chen.fragment.Notice_fragment2;
import com.chen.handle.DataInput;
import com.chen.handle.HttpUtil;
import com.chen.handle.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//通知 实体类
public class Notice extends DataSupport {
    private static final String TAG="notice";
    private static List<Notice> notices=new ArrayList<>();
    private static List<Notice> not_read_notices=new ArrayList<>();
    private static List<Notice> read_notices=new ArrayList<>();

    private String title;//通知标题
    private String content;//通知内容
    private String author;//发送人
    private String date;//发送日期
    private boolean read=false;//是否已读
    private int notice_id;//索引

    private Notice(String title,String content,String author,MyDate date,int id){
        this.title=title;
        this.content=content;
        this.author=author;
        this.date=date.toString();
        this.notice_id =id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getExtraMsg(){
        return author+"  "+date;
    }
    private int getNotice_id() {
        return notice_id;
    }
    public boolean isRead() {
        return read;
    }

    public void read(){
        read=true;
    }

    public static List<Notice> getNotices() {
        return notices;
    }
    public static List<Notice> getNot_read_notices() {
        return not_read_notices;
    }
    public static List<Notice> getRead_notices() {
        return read_notices;
    }

    public static void initNotices(){
        //初始化通知列表
        /*
        //测试
        for(int i=1;i<=20;i++) {
            String t = "", c = "";
            for (int k = 1; k <= 30; k++) {
                t += (i + "");
            }
            for (int k = 1; k <= 1000; k++) {
                c += (i + "");
            }
            Notice n = new Notice(t, c, "陈家耀", new MyDate(2020, 1, 23, 14, 23),i);
            notices.add(n);
        }
        //测试
         */
        notices= DataInput.getNotices();
        updateNotices(null,null,false);
    }
    public static void updateNotices(final NoticeAdapter adapter, final FragmentActivity activity, final boolean choose_read){
        //更新通知列表
        StringBuilder builder=new StringBuilder();
        String now_id;
        if(notices.size()>0){
            for(int i=0;i<notices.size();i++){
                builder.append(notices.get(i).getNotice_id());
                if(i!=notices.size()-1){
                    builder.append("_");
                }
            }
        }
        now_id=builder.toString();
        //利用now_id向服务器获取通知列表
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_notices"));
        args.add(new HttpUtil.Arg("class",DataInput.getClassNumber()));
        args.add(new HttpUtil.Arg("now_id",now_id));
        HttpUtil.sendPostHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"获取通知列表失败",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result= URLDecoder.decode(response.body().string(),"utf-8");
                try{
                    JSONArray array=new JSONArray(result);
                    int n=array.length();
                    if(array.getJSONObject(0).getString("status").equals("ok")){
                        //如果班级里发过通知
                        String delete=array.getJSONObject(1).getString("id_list");
                        if(!delete.equals("")){
                            //清除已删除的通知
                            String[] ids=delete.split("_");
                            int p=0;
                            for(String id:ids){
                                int i=Integer.parseInt(id);
                                if(p>=notices.size()){
                                    break;
                                }
                                Notice notice=notices.get(p);
                                if(notice.getNotice_id()==i){
                                    notices.remove(p);
                                }else {
                                    p++;
                                }
                            }
                        }
                        for(int i=2;i<n;i++){
                            JSONObject object=array.getJSONObject(i);
                            String title=object.getString("title");
                            String author=object.getString("author");
                            String time=object.getString("time");
                            String content=object.getString("content");
                            String id=object.getString("id");
                            Notice notice=new Notice(title,content,author,MyDate.parseMyDate(time),Integer.parseInt(id));
                            notices.add(notice);
                        }
                    }else{
                        //如果班级里没发过通知
                        notices=new ArrayList<>();
                    }
                    read_notices=new ArrayList<>();
                    not_read_notices=new ArrayList<>();
                    for(Notice notice:notices){
                        if(notice.isRead()){
                            read_notices.add(notice);
                        }else {
                            not_read_notices.add(notice);
                        }
                    }
                    //更新界面，显示提示
                    if(adapter!=null){
                        if(choose_read){
                            adapter.setNotices(read_notices);
                        }else{
                            adapter.setNotices(not_read_notices);
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        if(activity instanceof BaseActivity){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.replaceFragment(activity, R.id.main_fragment,new Notice_fragment());
                                }
                            });
                        }else if(activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(choose_read){
                                        Util.replaceFragment(activity, R.id.notice_fragment,new Notice_fragment2(read_notices));
                                    }else{
                                        Util.replaceFragment(activity, R.id.notice_fragment,new Notice_fragment2(not_read_notices));
                                    }
                                }
                            });
                        }
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"获取通知列表失败",TAG,true);
                }
            }
        });
    }
}
