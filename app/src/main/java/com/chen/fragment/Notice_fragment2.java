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
import com.chen.adapter.NoticeAdapter;
import com.chen.data.Notice;
import com.chen.handle.Util;

import java.util.List;

//"通知"子碎片
public class Notice_fragment2 extends Fragment {
    private List<Notice> noticeList;
    private NoticeAdapter adapter;

    public Notice_fragment2(List<Notice> noticeList){
        this.noticeList=noticeList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.notice_recyclerview,container,false);
        //用recyclerView显示通知项目
        RecyclerView recyclerView=view.findViewById(R.id.notice_recyclerView);
        adapter=new NoticeAdapter(noticeList,getActivity());
        Util.loadRecyclerView(recyclerView,new LinearLayoutManager(getActivity()),adapter);
        //用recyclerView显示通知项目
        return view;
    }
    public NoticeAdapter getAdapter() {
        return adapter;
    }
}
