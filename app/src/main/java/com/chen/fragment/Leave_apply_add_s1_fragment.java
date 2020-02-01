package com.chen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.activity.R;
import com.chen.data.LeaveApplication;
import com.chen.handle.DataInput;
import com.chen.handle.DataOutput;

//创建请假申请步骤一 fragment
public class Leave_apply_add_s1_fragment extends Fragment {
    private EditText title;//请假申请的标题
    private EditText content;//请假申请的内容

    public String getTitle() {
        return title.getText().toString();
    }
    public String getContent() {
        return content.getText().toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.leave_apply_add_s1,container,false);
        title=view.findViewById(R.id.leave_apply_add_s1_title);
        content=view.findViewById(R.id.leave_apply_add_s1_content);
        input();
        return view;
    }
    @Override
    public void onDestroy() {
            super.onDestroy();
            save();
    }

    private void input(){
        //获得已经保存的数据
        String t=DataInput.getNowApplication(getActivity(), LeaveApplication.KEY_TITLE),
                c=DataInput.getNowApplication(getActivity(),LeaveApplication.KEY_CONTENT);
        if(t!=null){
            title.setText(t);
        }
        if(c!=null){
            content.setText(c);
        }
    }
    private void save(){
        //保存数据
        String t=title.getText().toString(),
                c=content.getText().toString();
        if(!t.equals("")){
            DataOutput.saveNowApplication(getActivity(),LeaveApplication.KEY_TITLE,t);
        }
        if(!c.equals("")){
            DataOutput.saveNowApplication(getActivity(),LeaveApplication.KEY_CONTENT,c);
        }
    }
}
