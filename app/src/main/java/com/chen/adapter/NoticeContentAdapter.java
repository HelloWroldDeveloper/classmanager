package com.chen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;
//通知内容的适配器
public class NoticeContentAdapter extends RecyclerView.Adapter<NoticeContentAdapter.ViewHolder>{
    private String content;//通知内容
    private String extra_msg;//通知的附加信息(发送人和发送日期)
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView notice_content;
        TextView notice_msg;
        public ViewHolder(View view){
            super(view);
            notice_content=(TextView)view.findViewById(R.id.notice_content_text);
            notice_msg=(TextView)view.findViewById(R.id.notice_content_msg);
        }
    }
    public NoticeContentAdapter(String content,String extra_msg){
        this.content=content;
        this.extra_msg=extra_msg;
    }
    @NonNull
    @Override
    public NoticeContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_content,parent,false);
        NoticeContentAdapter.ViewHolder holder=new NoticeContentAdapter.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull NoticeContentAdapter.ViewHolder holder, int position) {
        holder.notice_content.setText(content);
        holder.notice_msg.setText(extra_msg);
    }
    @Override
    public int getItemCount(){
        return 1;
    }
}
