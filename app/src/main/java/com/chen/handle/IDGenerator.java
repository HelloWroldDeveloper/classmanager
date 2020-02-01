package com.chen.handle;


import java.util.ArrayList;
import java.util.List;

//ID生成 实用类
public class IDGenerator {
    public static List<ID> ID_list=new ArrayList<ID>();

    public static void initGenerator(){
        //初始化ID_list
        //初始化ID_list
    }
    public static void saveGenerator(){
        //保存ID_list
        //保存ID_list
    }
    public static int generationID(String name_space){
        //根据传入的命名空间获取唯一的id
        ID found_id=null;
        for (ID id:ID_list){
            if(id.getName_space().equals(name_space)){
                found_id=id;
                break;
            }
        }
        if (found_id!=null){
            found_id.now_id_plus();
        }else{
            found_id=new IDGenerator.ID(name_space,1);
            ID_list.add(found_id);
        }
        return found_id.getNow_id();
    }
    //id 实体内部类
    static class ID{
        private String name_space;
        private int now_id;
        ID(String name_space,int now_id){
            this.name_space=name_space;
            this.now_id=now_id;
        }
        public String getName_space(){
            return name_space;
        }
        public int getNow_id(){
            return now_id;
        }
        public void now_id_plus(){
            this.now_id++;
        }
    }
}
