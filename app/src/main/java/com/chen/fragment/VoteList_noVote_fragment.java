package com.chen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chen.activity.R;
import com.chen.handle.ScreenUtil;

//无投票事件时的"投票列表"碎片
public class VoteList_noVote_fragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScreenUtil.adapterScreen(getActivity(),480,false);
        return inflater.inflate(R.layout.vote_list_f2,container,false);
    }
}
