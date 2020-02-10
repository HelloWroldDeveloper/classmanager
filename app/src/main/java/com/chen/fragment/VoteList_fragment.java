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
import com.chen.adapter.VoteAdapter;
import com.chen.data.Vote;
import com.chen.handle.Util;

import java.util.List;

//有投票事件时的"投票列表"碎片
public class VoteList_fragment extends Fragment {
    private List<Vote> votes;
    private VoteAdapter adapter;//投票事件列表 适配器
    private boolean is_me;//是否当前用户创建的投票

    public VoteList_fragment(List<Vote> votes,boolean is_me){
        super();
        this.votes=votes;
        this.is_me=is_me;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.vote_list_f1,container,false);
        //初始化recyclerView
        RecyclerView recyclerView=view.findViewById(R.id.vote_list_f1_recyclerview);
        adapter=new VoteAdapter(getActivity(),votes,is_me);
        Util.loadRecyclerView(recyclerView,new LinearLayoutManager(getActivity()),adapter);
        //初始化recyclerView
        return view;
    }
    public VoteAdapter getAdapter() {
        return adapter;
    }
}
