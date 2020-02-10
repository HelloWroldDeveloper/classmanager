package com.chen.data;

import android.content.Intent;
import android.widget.Toast;

import com.chen.activity.BaseActivity;
import com.chen.adapter.VoteAdapter;
import com.chen.handle.DataInput;
import com.chen.handle.DataOutput;
import com.chen.handle.HttpUtil;
import com.chen.handle.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//投票 实体类
public class Vote {
    public static class Choice{
        //选项 实体内部类
        private String content;
        private int favour;
        private int id;
        private boolean voted=false;

        Choice(String content,int favour,int id){
            this.content=content;
            this.id=id;
            this.favour=favour;
        }
        public String getContent() {
            return content;
        }
        public int getFavour() {
            return favour;
        }
        public int getId(){return id;}
        public boolean isVoted(){return voted;}

        public void setVoted(boolean voted) {
            this.voted = voted;
        }
    }
    private static final String TAG="Vote";

    private static List<Vote> voteList;//投票数据列表
    private static List<Vote> nowVoteList;//进行中的投票的列表
    private static List<Vote> overdueVoteList;//已过期的投票的列表
    private static List<Vote> myVoteList;//用户创建的投票列表
    private static boolean allow_update=true;//当前是否允许更新投票列表

    private String title;//标题
    private MyDate create_time;//创建时间
    private MyDate deadline;//截止日期
    private String initiator;//发起人
    private int id;
    private List<Choice> choices;//选项
    private boolean voted=false;//是否已投
    private int max_select;//最大可选项数(0表示无限制)

    private Vote(String title,MyDate deadline,MyDate create_time,int id,List<Choice> choices,int max_select,String initiator){
        this.title=title;
        this.create_time=create_time;
        this.deadline=deadline;
        this.initiator=initiator;
        this.id=id;
        this.choices=choices;
        this.max_select=max_select;
    }
    public String getTitle() {
        return title;
    }
    public MyDate getCreate_time() {
        return create_time;
    }
    public MyDate getDeadline() {
        return deadline;
    }
    public String getInitiator() {
        return initiator;
    }
    public int getId() {
        return id;
    }
    public List<Choice> getChoices() {
        return choices;
    }
    public boolean isVoted() {
        return voted;
    }
    public int getMax_select() {
        return max_select;
    }

    public void vote(){
        this.voted=true;
    }

    public static boolean isAllow_update() {
        return allow_update;
    }
    public static void setAllow_update(boolean allow_update) {
        Vote.allow_update = allow_update;
    }

    public static void initVote(){
        //初始化投票数据
        voteList=new ArrayList<>();
        updateVote(null,1,null);
    }

    /*public static List<Vote> getVoteList(){
        return voteList;
    }*/
    public static List<Vote> getNowVoteList(){
        return nowVoteList;
    }
    public static List<Vote> getOverdueVoteList(){
        return overdueVoteList;
    }
    public static List<Vote> getMyVoteList() {
        return myVoteList;
    }

    //通过投票id找到投票对象
    public static Vote findVoteByID(int id){
        int a=0,b=voteList.size()-1;
        while(true){
            Vote A=voteList.get(a),
                    B=voteList.get(b);
            if(A.getId()==id){
                return A;
            }else if(B.getId()==id){
                return B;
            }else if(A.getId()>id){
                break;
            }else if(B.getId()<id){
                break;
            }
            if (a-b==1||a-b==-1) {
                break;
            }
            int k=(a+b)/ 2;
            if (voteList.get(k).getId() > id) {
                b=k;
            }else{
                a=k;
            }
        }
        return null;
    }
    //创建新投票
    public static void create_vote(String title, List<String> contents, MyDate deadline, int max_select, BaseActivity p_activity, final BaseActivity activity){
        //用max_select,deadline,title和contents向服务器发出创建投票的请求，得到id
        User now_user=User.getNowUser();
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","add_vote"));
        args.add(new HttpUtil.Arg("class",DataInput.getClassNumber()));
        args.add(new HttpUtil.Arg("title",title));
        args.add(new HttpUtil.Arg("create_date",MyDate.getCurrentDate().toString()));
        args.add(new HttpUtil.Arg("deadline",deadline.toString()));
        args.add(new HttpUtil.Arg("initiator",now_user.getName()));
        args.add(new HttpUtil.Arg("max_select",max_select+""));
        args.add(new HttpUtil.Arg("max_selectcount",contents.size()+""));
        for(int i=0;i<contents.size();i++){
            args.add(new HttpUtil.Arg("content"+i,contents.get(i)));
        }
        DataOutput.deleteNowVote(p_activity);//清除用户当前创建的投票选项和标题的缓存
        HttpUtil.sendPostHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"创建投票时发生错误",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    int id=Integer.parseInt(new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("id"));
                    DataOutput.addVoteID(id);
                    Intent intent=new Intent();
                    intent.putExtra("return",true);
                    activity.setResult(BaseActivity.RESULT_OK,intent);
                    activity.finish();
                }catch (Exception e){
                    Util.displayError(activity,e,"创建投票时发生错误",TAG,true);
                }
            }
        });
    }
    //删除投票
    public static void deleteVote(int id, final BaseActivity activity){
        DataOutput.deleteVotedID(id);
        DataOutput.deleteVoteID(id);
        //根据id向服务器提出删除投票的请求
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","delete_vote"));
        args.add(new HttpUtil.Arg("id",""+id));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"删除投票时发生错误",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response){
                try{
                    String status=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("status");
                    if(status.equals("ok")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"成功删除投票",Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        });
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"找不到对应的投票",Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        });
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"删除投票时发生错误",TAG,true);
                }
            }
        });
    }
    //更新投票列表
    public static void updateVote(final BaseActivity activity, final int choose, final VoteAdapter adapter){
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","check_vote"));
        args.add(new HttpUtil.Arg("class",DataInput.getClassNumber()));
        HttpUtil.sendGetHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"更新投票列表失败",TAG,false);
            }
            @Override
            public void onResponse(Call call, Response response) {
                try{
                    nowVoteList=new ArrayList<>();
                    overdueVoteList=new ArrayList<>();
                    myVoteList=new ArrayList<>();
                    voteList=new ArrayList<>();
                    //从服务器获取最新的投票数据，保存到voteList中
                    JSONArray array=new JSONArray(URLDecoder.decode(response.body().string(),"utf-8"));
                    String status=array.getJSONObject(0).getString("status");
                    if(status.equals("ok")){
                        int n=array.length();
                        for(int i=1;i<n;i++){
                            JSONArray vote=array.getJSONArray(i);
                            JSONObject vote_msg=vote.getJSONObject(0);
                            int m=vote.length();
                            String title=vote_msg.getString("title");
                            String create_date=vote_msg.getString("create_date");
                            String deadline=vote_msg.getString("deadline");
                            String initiator=vote_msg.getString("initiator");
                            String max_select=vote_msg.getString("max_select");
                            String id=vote_msg.getString("id");
                            List<Choice> choices=new ArrayList<>();
                            for(int j=1;j<m;j++){
                                JSONObject item=vote.getJSONObject(j);
                                String content_id=item.getString("id");
                                String content=item.getString("content");
                                String favour=item.getString("favour");
                                Choice choice=new Choice(content,Integer.parseInt(favour),Integer.parseInt(content_id));
                                choices.add(choice);
                            }
                            Vote v=new Vote(title,MyDate.parseMyDate(deadline),MyDate.parseMyDate(create_date),Integer.parseInt(id),
                                    choices,Integer.parseInt(max_select),initiator);
                            voteList.add(v);
                        }
                    }else {
                        if(activity!=null){
                            Util.recreateActivity(activity);
                        }
                        return;
                    }
                    //从服务器获取最新的投票数据
                    //把"进行中"、"已过期"和"用户创建"的投票和"已投"、"未投"的投票分开
                    List<Integer> user_ids= DataInput.getVoteIDs();
                    List<Integer> voted_ids=DataInput.getVotedIDs();
                    for (Vote vote:voteList){
                        //判断投票是否过期
                        if(MyDate.before_now(vote.getDeadline())){
                            overdueVoteList.add(vote);
                        }else {
                            nowVoteList.add(vote);
                        }
                    }
                    //整理出用户创建的投票
                    int a=0,b;
                    boolean flag=true;
                    for(int i=0;i<user_ids.size()&&flag;i++){
                        int now_search_id=user_ids.get(i);
                        b=voteList.size()-1;
                        while(true){
                            Vote A=voteList.get(a),
                                    B=voteList.get(b);
                            if(A.getId()==now_search_id){
                                myVoteList.add(A);
                                break;
                            }else if(B.getId()==now_search_id){
                                myVoteList.add(B);
                                break;
                            }else if(A.getId()>now_search_id){
                                break;
                            }else if(B.getId()<now_search_id){
                                flag=false;break;
                            }
                            if (a-b==1||a-b==-1) {
                                break;
                            }
                            int k=(a+b)/ 2;
                            if (voteList.get(k).getId() > now_search_id) {
                                b=k;
                            }else{
                                a=k;
                            }
                        }
                    }
                    //整理出用户创建的投票
                    a=0;flag=true;
                    //整理出用户已投的投票
                    for(int i=0;i<voted_ids.size()&&flag;i++){
                        int now_search_id=voted_ids.get(i);
                        b=voteList.size()-1;
                        while(true){
                            Vote A=voteList.get(a),
                                    B=voteList.get(b);
                            if(A.getId()==now_search_id){
                                A.vote();
                                break;
                            }else if(B.getId()==now_search_id){
                                B.vote();
                                break;
                            }else if(A.getId()>now_search_id){
                                break;
                            }else if(B.getId()<now_search_id){
                                flag=false;break;
                            }
                            if (a-b==1||a-b==-1) {
                                break;
                            }
                            int k=(a+b)/ 2;
                            if (voteList.get(k).getId() > now_search_id) {
                                b=k;
                            }else{
                                a=k;
                            }
                        }
                    }
                    //整理出用户已投的投票
                    if(activity!=null){
                        if(adapter!=null){
                            if (choose==1){
                                adapter.setVotes(Vote.getNowVoteList());
                            }else if (choose==2){
                                adapter.setVotes(Vote.getOverdueVoteList());
                            }else {
                                adapter.setVotes(Vote.getMyVoteList());
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Util.recreateActivity(activity);
                        }
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"更新投票列表失败",TAG,false);
                }
            }
        });
    }
    //执行投票
    public static void execute_vote(final BaseActivity activity, final Vote vote, List<Choice> choices){
        int vote_id=vote.getId();
        DataOutput.addVotedID(vote_id);
        StringBuilder builder=new StringBuilder();
        String content_ids;
        for(Choice c:choices){
            if(c.isVoted()){
                builder.append(c.getId());
                builder.append("_");
            }
        }
        content_ids=builder.substring(0,builder.length()-1);
        //利用content_ids和vote_id发送请求
        List<HttpUtil.Arg> args=new ArrayList<>();
        args.add(new HttpUtil.Arg("type","execute_vote"));
        args.add(new HttpUtil.Arg("content_ids",content_ids));
        args.add(new HttpUtil.Arg("event_id",vote_id+""));
        HttpUtil.sendPostHttpRequest(args, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.displayError(activity,e,"投票时发生错误",TAG,true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String status=new JSONObject(URLDecoder.decode(response.body().string(),"utf-8")).getString("status");
                    if(status.equals("ok")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vote.vote();
                                Toast.makeText(activity,"投票成功",Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        });
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"找不到对应的投票选项",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    Util.displayError(activity,e,"投票时发生错误",TAG,true);
                }
            }
        });
    }
}
