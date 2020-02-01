package com.chen.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.activity.R;
import com.chen.data.MemberStyle;
import com.chen.data.User;

import java.util.List;

//班级成员列表的适配器
public class ClassMembersAdapter extends RecyclerView.Adapter<ClassMembersAdapter.ViewHolderParent>{
    private List<MemberStyle> styles;//项目列表
    private Activity activity;

    public ClassMembersAdapter(List<MemberStyle> styles,Activity activity){
        this.styles=styles;
        this.activity=activity;
    }
    //子项基类 内部类
    static class ViewHolderParent extends RecyclerView.ViewHolder{
        TextView member_number;
        TextView member_name;
        TextView tip;
        public ViewHolderParent(View view){
            super(view);
        }
    }
    //"用户"子项 内部类
    static class ViewHolderUser extends ViewHolderParent{
        public ViewHolderUser(View view){
            super(view);
            this.member_name=(TextView)view.findViewById(R.id.class_member_name);
            this.member_number=(TextView)view.findViewById(R.id.class_member_number);
        }
    }
    //"提示"子项 内部类
    static class ViewHolderTip extends ViewHolderParent{
        public ViewHolderTip(View view){
            super(view);
            this.tip=(TextView)view.findViewById(R.id.class_member_tip);
        }
    }
    //"空白"子项 内部类
    static class ViewHolderWhite extends ViewHolderParent{
        public ViewHolderWhite(View view){
            super(view);
        }
    }

    @NonNull
    @Override
    public ViewHolderParent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderParent viewHolder;
        if(viewType==MemberStyle.STYLE_USER_COMMON){
            viewHolder=new ViewHolderUser(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_members_card_common,parent,false));
        }else if(viewType==MemberStyle.STYLE_USER_ME){
            viewHolder=new ViewHolderUser(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_members_card_me,parent,false));
        }else if((viewType==MemberStyle.STYLE_TIP_COMMON_USER)||(viewType==MemberStyle.STYLE_TIP_ADMIN_USER)){
            viewHolder=new ViewHolderTip(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_members_tip,parent,false));
        }else{
            viewHolder=new ViewHolderWhite(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_members_white,parent,false));
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderParent holder, int position) {
        MemberStyle member=styles.get(position);
        if((member.getType()==MemberStyle.STYLE_USER_COMMON)||(member.getType()==MemberStyle.STYLE_USER_ME)){
            User user=member.getUser();
            holder.member_name.setText(user.getName());
            holder.member_number.setText(user.getNumber());
        }else if(member.getType()==MemberStyle.STYLE_TIP_COMMON_USER){
            holder.tip.setText("普通成员");
        }else if(member.getType()==MemberStyle.STYLE_TIP_ADMIN_USER){
            holder.tip.setText("管理员");
        }
    }
    @Override
    public int getItemCount() {
        return styles.size();
    }
    @Override
    public int getItemViewType(int position) {
        return styles.get(position).getType();
    }

    public void setStyles(List<MemberStyle> styles) {
        this.styles = styles;
    }
}
