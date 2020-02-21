package com.chen.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.activity.R;
import com.chen.adapter.NoticeAdapter;
import com.chen.data.Notice;
import com.chen.handle.DataOutput;
import com.chen.handle.ScreenUtil;
import com.chen.handle.Util;

import java.util.List;
//"通知"碎片
public class Notice_fragment extends BaseFragment {
    private boolean choose_read=false;
    private NoticeAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScreenUtil.adapterScreen(getActivity(),480,false);
        View view=inflater.inflate(R.layout.notice,container,false);
        final TextView not_read=view.findViewById(R.id.not_read);
        final TextView read=view.findViewById(R.id.read);
        not_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean old_choose_read=choose_read;
                choose_read=false;
                not_read.setTextColor(Color.RED);
                read.setTextColor(Color.BLACK);
                check_not_read_notices();
                if(old_choose_read){
                    Notice.updateNotices(adapter,getActivity(),choose_read);
                }
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean old_choose_read=choose_read;
                choose_read=true;
                not_read.setTextColor(Color.BLACK);
                read.setTextColor(Color.RED);
                check_read_notices();
                if(old_choose_read){
                    Notice.updateNotices(adapter,getActivity(),choose_read);
                }
            }
        });
        check_not_read_notices();
        return view;
    }

    private void check_read_notices(){
        //显示"已读"消息
        List<Notice> read_notice=Notice.getRead_notices();
        if(read_notice.size()>0){
            Notice_fragment2 fragment=new Notice_fragment2(read_notice);
            adapter=fragment.getAdapter();
            Util.replaceFragment(getActivity(),R.id.notice_fragment,fragment);
        }else {
            adapter=null;
            Util.replaceFragment(getActivity(),R.id.notice_fragment,new No_notice_fragment());
        }
    }
    private void check_not_read_notices(){
        //显示"未读"消息
        List<Notice> not_read_notice=Notice.getNot_read_notices();
        if(not_read_notice.size()>0){
            Notice_fragment2 fragment=new Notice_fragment2(not_read_notice);
            adapter=fragment.getAdapter();
            Util.replaceFragment(getActivity(),R.id.notice_fragment,fragment);
        }else {
            adapter=null;
            Util.replaceFragment(getActivity(),R.id.notice_fragment,new No_notice_fragment());
        }
    }

    @Override
    public void onResume() {
        //更新通知项目列表
        super.onResume();
        Notice.updateNotices(adapter,getActivity(),choose_read);//更新通知列表
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        DataOutput.saveNotices();
    }
}