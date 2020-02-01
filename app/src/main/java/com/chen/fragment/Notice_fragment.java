package com.chen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;
import com.chen.adapter.NoticeAdapter;
import com.chen.data.Notice;

import java.util.List;
//"通知"碎片
public class Notice_fragment extends Fragment {
    private NoticeAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        List<Notice> notices=Notice.getLatestNotices();//获得最新的通知列表
        View view;
        if(notices.size()>0){
            //用recyclerview显示通知项目
            view=inflater.inflate(R.layout.notice,container,false);
            RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.notice_recyclerView);
            LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter=new NoticeAdapter(notices,getActivity());
            recyclerView.setAdapter(adapter);
            //用recyclerview显示通知项目
        }else{
            //如果没有通知，就显示"当前没有任何通知"
            view=inflater.inflate(R.layout.no_notices,container,false);
        }
        return view;
    }
    @Override
    public void onResume() {
        //更新通知项目列表
        super.onResume();
        adapter.setNotices(Notice.getLatestNotices());
        adapter.notifyDataSetChanged();
    }
}