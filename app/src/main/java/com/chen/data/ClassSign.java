package com.chen.data;

import android.util.Log;

import java.text.ParseException;
import java.util.List;

public class ClassSign {
    public static class StudentSign{
        private String name;
        private MyDate sign_time;
        public StudentSign(String name,MyDate sign_time){
            this.name=name;
            this.sign_time=sign_time;
        }
        public String getName() {
            return name;
        }
        public MyDate getSign_time() {
            return sign_time;
        }
    }

    public static final String TAG="ClassSign";//用于生成日志的tag
    public static final int NOT_SIGNED=1;//状态：未签到
    public static final int SIGNED=2;//状态：签到成功
    public static final int FAILED=3;//状态：签到失败

    private static ClassSign now_sign=null;//当前的签到

    private String place;//签到地点
    private String initiator;//发起人
    private double[] center;//签到中心(用于核验用户是否签到成功) 第一个是纬度，第二个是经度
    private int distance;//允许的最大签到距离半径
    private MyDate deadline;//签到的截止时间
    private int status;//签到状态
    private int id;//签到在服务器sql中的id

    public ClassSign(String place,String initiator,double[] center,int distance,MyDate deadline,int status,int id){
        this.place=place;
        this.initiator=initiator;
        this.center=center;
        this.distance=distance;
        this.deadline=deadline;
        this.status=status;
        this.id=id;
    }

    public String getPlace() {
        return place;
    }
    public String getInitiator() {
        return initiator;
    }
    public double[] getCenter() {
        return center;
    }
    public int getDistance() {
        return distance;
    }
    public MyDate getDeadline() {
        return deadline;
    }
    public int getStatus() {
        return status;
    }

    public static ClassSign searchClassSign(){
        if(now_sign==null){
            //从服务器检索对应于当前用户的签到(在此书写逻辑)

            //从服务器检索对应于当前用户的签到(在此书写逻辑)
        }else {
            try{
                //判断一下当前签到是否过期,过期就重新从服务器检索
                if(MyDate.before_now(now_sign.getDeadline())){
                    now_sign=null;
                    now_sign=searchClassSign();
                }
            }catch (ParseException e){
                e.printStackTrace();
                Log.e(TAG,"无法解析日期");
                return null;
            }
        }
        return now_sign;
    }
    public static void initSign(){
        //本方法仅供测试使用
        now_sign=new ClassSign("测试地点","陈家耀",new double[]{23.235556,113.295},1000,new MyDate(2020,2,3,9,0)
                ,ClassSign.NOT_SIGNED,1);
    }

    public void sign_succeed(){
        this.status=ClassSign.SIGNED;
        //签到成功,告诉服务器xxx在xx时间对id为xx的签到签到成功(在此书写逻辑)

        //签到成功,告诉服务器xxx在xx时间对id为xx的签到签到成功(在此书写逻辑)
    }
    public void sign_fail(){
        this.status=ClassSign.FAILED;
        //签到失败,告诉服务器xxx在xx时间对id为xx的签到签到失败(在此书写逻辑)

        //签到失败,告诉服务器xxx在xx时间对id为xx的签到签到失败(在此书写逻辑)
    }
    public List<ClassSign.StudentSign> getSignedStudents(){
        List<ClassSign.StudentSign> students=null;
        //通过当前签到的id从服务器获取已签到的人(在此书写逻辑)

        //通过当前签到的id从服务器获取已签到的人(在此书写逻辑)
        return students;
    }
    public List<ClassSign.StudentSign> getUnsignedStudents(){
        List<ClassSign.StudentSign> students=null;
        //通过当前签到的id从服务器获取未签到的人(在此书写逻辑)

        //通过当前签到的id从服务器获取未签到的人(在此书写逻辑)
        return students;
    }
}
