package com.chen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;
import com.chen.adapter.VoteAddAdapter;
import com.chen.handle.Util;

import java.util.List;

//有投票选项时的"添加投票"碎片
public class Vote_add_fragment1 extends Fragment {
    private List<String> vote_contents;//投票选项 列表
    private VoteAddAdapter adapter;//投票选项列表 适配器

    public Vote_add_fragment1(List<String> vote_contents){
        this.vote_contents=vote_contents;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.vote_add_f1,container,false);
        //加载recyclerView
        RecyclerView recyclerView=view.findViewById(R.id.vote_add_f1_recyclerView);
        adapter=new VoteAddAdapter(vote_contents);
        Util.loadRecyclerView(recyclerView,new LinearLayoutManager(getActivity()),adapter);
        //加载recyclerView
        return view;
    }

    //添加 投票选项
    public List<String> addContent(){
        vote_contents=adapter.getContents();
        vote_contents.add("");
        adapter.setContents(vote_contents);
        adapter.notifyDataSetChanged();
        return vote_contents;
    }
    //删除 投票选项
    public List<String> deleteContent(){
        vote_contents=adapter.getContents();
        vote_contents.remove(vote_contents.size()-1);
        adapter.setContents(vote_contents);
        adapter.notifyDataSetChanged();
        return vote_contents;
    }
    //获取 投票选项
    public List<String> getContents(){
        return adapter.getContents();
    }
}
