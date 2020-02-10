package com.chen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;
import com.chen.data.Vote;

import java.util.List;

//已投情况下的投票详情 适配器
public class VoteContentAdapter2 extends RecyclerView.Adapter<VoteContentAdapter2.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView content;
        private TextView favour;
        public ViewHolder(View view){
            super(view);
            content=view.findViewById(R.id.vote_content2_item_content);
            favour=view.findViewById(R.id.vote_content2_item_favour);
        }
    }

    private List<Vote.Choice> choices;//投票选项

    public VoteContentAdapter2(List<Vote.Choice> choices){this.choices=choices;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_content2_item,parent,false);
        return  new VoteContentAdapter2.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VoteContentAdapter2.ViewHolder holder,int position) {
        Vote.Choice choice=choices.get(position);
        holder.favour.setText(choice.getFavour()+"人");
        holder.content.setText(choice.getContent());
    }
    @Override
    public int getItemCount() {
        return choices.size();
    }
}
