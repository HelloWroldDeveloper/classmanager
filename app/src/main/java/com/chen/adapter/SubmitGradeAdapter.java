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

import com.chen.activity.R;
import com.chen.activity.SubmitGradeContentActivity;
import com.chen.data.Grade;

import java.util.List;
//上传成绩项目列表的适配器
public class SubmitGradeAdapter extends RecyclerView.Adapter<SubmitGradeAdapter.ViewHolder>{
    private List<Grade> grades;
    private Activity activity;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout item;
        LinearLayout layout;
        TextView test_name;
        TextView extra_msg;
        TextView deadline;
        public ViewHolder(View view){
            super(view);
            item=view.findViewById(R.id.submit_grade_item);
            test_name=view.findViewById(R.id.submit_grade_test_name);
            extra_msg=view.findViewById(R.id.submit_grade_extra_msg);
            deadline=view.findViewById(R.id.submit_grade_deadline);
            layout=view.findViewById(R.id.submit_grade_layout);
        }
    }
    public SubmitGradeAdapter(List<Grade> grades, Activity activity){
        this.grades=grades;
        this.activity=activity;
    }
    @NonNull
    @Override
    public SubmitGradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.submit_grade_item,parent,false);
        return new SubmitGradeAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final SubmitGradeAdapter.ViewHolder holder, final int position) {
        final Grade grade=grades.get(position);
        holder.test_name.setText(grade.getTest_name());
        if(grade.getStatus()==Grade.NOT_SUBMITTED){
            holder.extra_msg.setText(grade.getExtraMsg()+" 未提交");
            holder.item.setEnabled(true);
            holder.layout.setBackgroundColor(Color.parseColor("#E1FFFF"));
            holder.extra_msg.setBackgroundColor(Color.parseColor("#D4F2E7"));
            holder.deadline.setBackgroundColor(Color.parseColor("#D4F2E7"));
        }else if(grade.getStatus()==Grade.SUBMITTED){
            holder.extra_msg.setText(grade.getExtraMsg()+" 已提交(可修改)");
            holder.item.setEnabled(true);
            holder.layout.setBackgroundColor(Color.parseColor("#E1FFFF"));
            holder.extra_msg.setBackgroundColor(Color.parseColor("#D4F2E7"));
            holder.deadline.setBackgroundColor(Color.parseColor("#D4F2E7"));
        }else{
            holder.extra_msg.setText(grade.getExtraMsg()+" 已过期");
            holder.layout.setBackgroundColor(Color.parseColor("#808080"));
            holder.extra_msg.setBackgroundColor(Color.parseColor("#808080"));
            holder.deadline.setBackgroundColor(Color.parseColor("#808080"));
        }
        holder.deadline.setText(grade.getDeadline_msg()+"过期");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitGradeContentActivity.actionStart(activity,grade);
            }
        });
    }
    @Override
    public int getItemCount(){
        return grades.size();
    }
    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
