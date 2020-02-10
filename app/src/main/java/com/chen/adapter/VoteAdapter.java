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
import com.chen.activity.VoteContentActivity;
import com.chen.data.Vote;

import java.util.List;

//投票事件列表 适配器
public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;//标题
        private TextView msg;//发起人+创建时间
        private TextView status;//投票状态
        private TextView deadline;//截止日期
        private LinearLayout item;//整个子项

        public ViewHolder(View view){
            super(view);
            title= view.findViewById(R.id.vote_item_title);
            msg=view.findViewById(R.id.vote_item_msg);
            status=view.findViewById(R.id.vote_item_status);
            deadline=view.findViewById(R.id.vote_item_deadline);
            item=view.findViewById(R.id.vote_item);
        }
    }

    private List<Vote> votes;//投票事件列表
    private Activity activity;
    private boolean is_me;//是否当前用户创建的投票

    public VoteAdapter(Activity activity,List<Vote> votes,boolean is_me){
        this.activity=activity;
        this.votes=votes;
        this.is_me=is_me;
    }

    @NonNull
    @Override
    public VoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_item,parent,false);
        return new VoteAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VoteAdapter.ViewHolder holder, int position) {
        final Vote vote=votes.get(position);
        holder.title.setText(vote.getTitle());
        holder.deadline.setText(vote.getDeadline().toString());
        holder.msg.setText(vote.getInitiator()+" "+vote.getCreate_time().toString());
        if(vote.isVoted()){
            holder.status.setText("已投");
            holder.status.setTextColor(Color.GREEN);
        }else{
            holder.status.setText("未投");
            holder.status.setTextColor(Color.RED);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //投票子项的点击事件处理
                VoteContentActivity.actionStart(activity,vote.getId(),is_me);
            }
        });
    }
    @Override
    public int getItemCount(){
        return votes.size();
    }
    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
