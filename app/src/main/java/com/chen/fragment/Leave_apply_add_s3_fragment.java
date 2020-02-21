package com.chen.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.activity.R;
import com.chen.data.LeaveApplication;
import com.chen.data.MyDate;
import com.chen.handle.DataInput;
import com.chen.handle.DataOutput;
import com.chen.handle.ScreenUtil;

//创建请假申请步骤三 fragment
public class Leave_apply_add_s3_fragment extends BaseFragment{
    private boolean creating=false;

    private TextView start_date_text;
    private TextView end_date_text;
    private TextView start_time_text;
    private TextView end_time_text;

    private int[] start_d=null;
    private int[] end_d=null;
    private int[] start_t=null;
    private int[] end_t=null;

    public int[] getStart_date(){
        return start_d;
    }
    public int[] getEnd_date(){
        return end_d;
    }
    public int[] getStart_time(){
        return start_t;
    }
    public int[] getEnd_time(){
        return end_t;
    }

    public void setCreating(boolean creating) {
        this.creating = creating;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScreenUtil.adapterScreen(getActivity(),480,false);
        View view=inflater.inflate(R.layout.leave_apply_add_s3,container,false);
        start_date_text=view.findViewById(R.id.leave_apply_add_s3_start_date_text);
        end_date_text=view.findViewById(R.id.leave_apply_add_s3_end_date_text);
        start_time_text=view.findViewById(R.id.leave_apply_add_s3_start_time_text);
        end_time_text=view.findViewById(R.id.leave_apply_add_s3_end_time_text);
        input();
        addEvent();
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!creating){
            save();
        }
    }

    private void addEvent(){
        start_date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_date(true);
            }
        });
        end_date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_date(false);
            }
        });
        start_time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_time(true);
            }
        });
        end_time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_time(false);
            }
        });
    }

    //获取已保存的数据
    private void input(){
        String start_date= DataInput.getNowApplication(getActivity(),LeaveApplication.KEY_START_DATE);
        String end_date=DataInput.getNowApplication(getActivity(),LeaveApplication.KEY_END_DATE);
        String start_time=DataInput.getNowApplication(getActivity(),LeaveApplication.KEY_START_TIME);
        String end_time=DataInput.getNowApplication(getActivity(),LeaveApplication.KEY_END_TIME);
        if(start_date!=null){
            start_date_text.setText(start_date);
        }
        if(end_date!=null){
            end_date_text.setText(end_date);
        }
        if(start_time!=null){
            start_time_text.setText(start_time);
        }
        if(end_time!=null){
            end_time_text.setText(end_time);
        }
    }
    //保存数据
    private void save(){
        String start_date=start_date_text.getText().toString();
        String end_date=end_date_text.getText().toString();
        String start_time=start_time_text.getText().toString();
        String end_time=end_time_text.getText().toString();
        if(!start_date.equals("请选择日期")){
            DataOutput.saveNowApplication(getActivity(), LeaveApplication.KEY_START_DATE,start_date);
        }
        if(!end_date.equals("请选择日期")){
            DataOutput.saveNowApplication(getActivity(),LeaveApplication.KEY_END_DATE,end_date);
        }
        if(!start_time.equals("请选择时间")){
            DataOutput.saveNowApplication(getActivity(),LeaveApplication.KEY_START_TIME,start_time);
        }
        if(!end_time.equals("请选择时间")){
            DataOutput.saveNowApplication(getActivity(),LeaveApplication.KEY_END_TIME,end_time);
        }
    }
    //对用户输入的时间进行格式化
    /*private String format_time(String input){
        int r=0,k=1;
        char[] chars=input.toCharArray();
        for(int i=chars.length-1;i>=0;i--){
            r+=((chars[i]-48)*k);
            k*=10;
        }
        return r+"";
    }*/
    //对由日期选择器获取的日期进行格式化
    private String format_date(int input){
        return ""+(input<10 ? ("0"+input):input);
    }

    //执行"日期选择"
    private void choose_date(final boolean start_date){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (start_date){
                    start_date_text.setText(i+"年"+format_date(i1+1)+"月"+format_date(i2)+"日");
                    start_d=new int[3];
                    start_d[0]=i;
                    start_d[1]=i1+1;
                    start_d[2]=i2;
                }else {
                    end_date_text.setText(i+"年"+format_date(i1+1)+"月"+format_date(i2)+"日");
                    end_d=new int[3];
                    end_d[0]=i;
                    end_d[1]=i1+1;
                    end_d[2]=i2;
                }
            }
        };
        DatePickerDialog dialog;
        if(start_date&&start_d!=null){
            dialog=new DatePickerDialog(getActivity(), listener,start_d[0],start_d[1]-1,start_d[2]);
        }else if((!start_date)&&end_d!=null){
            dialog=new DatePickerDialog(getActivity(), listener,end_d[0],end_d[1]-1,end_d[2]);
        }else{
            MyDate now=MyDate.getCurrentDate();
            dialog=new DatePickerDialog(getActivity(), listener,now.getYear(),now.getMonth()-1,now.getDay());
        }
        dialog.show();
    }
    private void choose_time(final boolean start_time){
        TimePickerDialog dialog;
        TimePickerDialog.OnTimeSetListener listener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if(start_time){
                    start_time_text.setText(i+":"+i1);
                    start_t=new int[2];
                    start_t[0]=i;
                    start_t[1]=i1;
                }else {
                    end_time_text.setText(i+":"+i1);
                    end_t=new int[2];
                    end_t[0]=i;
                    end_t[1]=i1;
                }
            }
        };
        if(start_time&&start_t!=null){
            dialog=new TimePickerDialog(getActivity(),listener,start_t[0],start_t[1],true);
        }else if((!start_time)&&end_t!=null){
            dialog=new TimePickerDialog(getActivity(),listener,end_t[0],end_t[1],true);
        }else{
            MyDate now=MyDate.getCurrentDate();
            dialog=new TimePickerDialog(getActivity(),listener,now.getHour(),now.getMinute(),true);
        }
        dialog.show();
    }
}
