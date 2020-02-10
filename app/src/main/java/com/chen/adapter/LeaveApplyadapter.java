package com.chen.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.LeaveApplyContentActivity;
import com.chen.activity.R;
import com.chen.data.LeaveApplication;

import java.util.List;

//请假申请列表 适配器
public class LeaveApplyadapter extends RecyclerView.Adapter<LeaveApplyadapter.ViewHolder>{
    private List<LeaveApplication> applications;//请假申请 列表
    private Activity activity;

    public LeaveApplyadapter(List<LeaveApplication> applications,Activity activity){
        this.applications=applications;
        this.activity=activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;//请假申请 标题
        TextView date_start;//请假申请 开始日期
        TextView date_end;//请假申请 结束日期
        TextView status;//请假申请 状态
        LinearLayout item;//请假申请 子项

        public ViewHolder(View view){
            super(view);
            title= view.findViewById(R.id.leave_item_title);
            date_start=view.findViewById(R.id.leave_item_date_start);
            date_end=view.findViewById(R.id.leave_item_date_end);
            status=view.findViewById(R.id.leave_item_status);
            item=view.findViewById(R.id.leave_item);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_apply_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LeaveApplication application=applications.get(position);
        holder.title.setText(application.getTitle());
        holder.date_start.setText(application.getStart_date().toString());
        holder.date_end.setText(application.getEnd_date().toString());
        switch (application.getStatus()){
            case LeaveApplication.REVIEWING:
                holder.status.setText("审核中");
                holder.status.setTextColor(Color.BLACK);
                break;
            case LeaveApplication.PASSED:
                holder.status.setText("已通过");
                holder.status.setTextColor(Color.GREEN);
                break;
            case LeaveApplication.OVER_DUE:
                holder.status.setText("已过期");
                holder.status.setTextColor(Color.RED);
                break;
            case LeaveApplication.IGNORED:
                holder.status.setText("已忽略");
                holder.status.setTextColor(Color.GRAY);
                break;
            case LeaveApplication.NO_PASS:
                holder.status.setText("未通过");
                holder.status.setTextColor(Color.RED);
                break;
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //请假申请子项的点击事件处理
                LeaveApplyContentActivity.actionStart(activity,application);
            }
        });
    }
    @Override
    public int getItemCount() {
        return applications.size();
    }

    public void setApplications(List<LeaveApplication> applications) {
        this.applications = applications;
    }
}
