package com.chen.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//添加投票选项的列表 适配器
public class VoteAddAdapter extends RecyclerView.Adapter<VoteAddAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder{
        private EditText content;
        private int index;
        public ViewHolder(View view){
            super(view);
            content=view.findViewById(R.id.vote_add_item_text);
        }
    }

    private List<String> contents;//选项列表
    private Map<Integer,TextWatcher> watchers=new HashMap<>();//当前文本监听器的列表

    public VoteAddAdapter(List<String> contents){
        this.contents=contents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_add_item,parent,false);
        return  new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.index=position;
        holder.content.setHint("选项"+(position+1));
        EditText text=holder.content;
        text.setText(contents.get(position));
        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                contents.set(position,editable.toString());
            }
        };
        watchers.put(position,watcher);
        text.addTextChangedListener(watcher);
    }
    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        //把被回收的holder的监听器取消
        super.onViewRecycled(holder);
        holder.content.removeTextChangedListener(watchers.get(holder.index));
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public List<String> getContents() {
        return contents;
    }
    public void setContents(List<String> contents) {
        this.contents = contents;
    }
}
