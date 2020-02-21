package com.chen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.activity.R;
import com.chen.handle.ScreenUtil;

//无投票选项时的"添加投票"碎片
public class Vote_add_fragment2 extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScreenUtil.adapterScreen(getActivity(),480,false);
        return inflater.inflate(R.layout.vote_add_f2,container,false);
    }
}
