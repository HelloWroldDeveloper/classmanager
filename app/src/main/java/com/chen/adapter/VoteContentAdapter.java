package com.chen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;
import com.chen.data.Vote;
import java.util.List;

public class VoteContentAdapter extends RecyclerView.Adapter<VoteContentAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView content;
        private RadioButton select1;
        private CheckBox select2;
        public ViewHolder(View view,boolean multiplied_select){
            super(view);
            if(multiplied_select){
                select2=view.findViewById(R.id.vote_content1_item2_checkbox);
                content=view.findViewById(R.id.vote_content1_item2_content);
            }else{
                select1=view.findViewById(R.id.vote_content1_item_radiobtn);
                content=view.findViewById(R.id.vote_content1_item_content);
            }
        }
    }

    private int max_select;//最大可选项数
    private List<Vote.Choice> choices;//选项
    private int now_select_num=0;//当前选择项数

    public VoteContentAdapter(int max_select,List<Vote.Choice> choices){
        this.max_select=max_select;
        this.choices=choices;
        if(max_select==0){
            this.max_select=choices.size();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(max_select==1){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_content1_item,parent,false);
            return new VoteContentAdapter.ViewHolder(view,false);
        }else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_content1_item2,parent,false);
            return new VoteContentAdapter.ViewHolder(view,true);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Vote.Choice choice=choices.get(position);
        holder.content.setText(choice.getContent());
        holder.setIsRecyclable(false);
        if(max_select==1){
            RadioButton btn=holder.select1;
            btn.setChecked(choice.isVoted());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice.setVoted(true);
                    int id=choice.getId();
                    for(Vote.Choice c:choices){
                        if(c.getId()!=id){
                            c.setVoted(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }else{
            final CheckBox btn=holder.select2;
            btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        if(now_select_num==max_select){
                            btn.setChecked(false);
                        }else {
                            if(!choice.isVoted()){
                                now_select_num++;
                                choice.setVoted(true);
                            }
                        }
                    }else {
                        if(choice.isVoted()){
                            now_select_num--;
                            choice.setVoted(false);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public List<Vote.Choice> getChoices() {
        return choices;
    }
}
