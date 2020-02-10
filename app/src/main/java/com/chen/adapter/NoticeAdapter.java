package com.chen.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.NoticeContentActivity;
import com.chen.activity.R;
import com.chen.data.Notice;

import java.util.List;
//通知项目列表的适配器
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private List<Notice> notices;//通知列表
    private Activity activity;
    static class ViewHolder extends RecyclerView.ViewHolder{
        Button notice_titleBtn;
        TextView notice_msg;
        public ViewHolder(View view){
            super(view);
            notice_titleBtn=view.findViewById(R.id.notice_titleBtn);
            notice_msg=view.findViewById(R.id.notice_extraMsg);
        }
    }
    public NoticeAdapter(List<Notice> notices,Activity activity){
        this.notices=notices;//对于传入的notices,总会把已读消息放到最后面
        this.activity=activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        Notice notice=notices.get(position);
        holder.notice_titleBtn.setText(notice.getTitle());//设置消息的标题
        holder.notice_msg.setText(notice.getExtraMsg());//设置消息的附加信息(如发送人,发送日期)
        if(notice.isRead()){
            //如果消息已读
            holder.notice_titleBtn.setTextColor(Color.GRAY);//把打开消息的按钮的文本颜色设置为灰色
        }else{
            //如果消息未读
            holder.notice_titleBtn.setTextColor(Color.BLACK);//把打开消息的按钮的文本颜色设置为黑色
        }
        holder.notice_titleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notice notice=notices.get(position);
                notice.read();//将这条消息记为已读
                NoticeContentActivity.actionStart(activity,notice);//开启含有消息内容的新活动
            }
        });
    }
    @Override
    public int getItemCount(){
        return notices.size();
    }
    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }
}
