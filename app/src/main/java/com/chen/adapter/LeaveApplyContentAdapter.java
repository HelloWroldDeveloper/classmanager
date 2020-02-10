package com.chen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;
import com.chen.data.LeaveApplication;

//请假申请内容 适配器
public class LeaveApplyContentAdapter extends RecyclerView.Adapter<LeaveApplyContentAdapter.ViewHolder>{
    private LeaveApplication application;//当前的请假申请

    public LeaveApplyContentAdapter(LeaveApplication application){
        this.application=application;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView content;//内容
        private TextView start_date;//开始日期
        private TextView end_date;//结束日期
        private TextView create_date;//创建日期
        private TextView status;//状态

        public ViewHolder(View view){
            super(view);
            content=view.findViewById(R.id.leave_apply_content_text);
            start_date=view.findViewById(R.id.leave_apply_content_start_date);
            end_date=view.findViewById(R.id.leave_apply_content_end_date);
            create_date=view.findViewById(R.id.leave_apply_content_create_date);
            status=view.findViewById(R.id.leave_apply_content_status);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_apply_content,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(application.getContent());
        holder.start_date.setText(application.getStart_date().toString());
        holder.end_date.setText(application.getEnd_date().toString());
        holder.create_date.setText(application.getCreate_date().toString());
        holder.status.setText(application.getStatusStatement());
    }
    @Override
    public int getItemCount() {
        return 1;
    }
    public void setApplication(LeaveApplication application) {
        this.application = application;
    }
}
