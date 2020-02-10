package com.chen.data;

import org.litepal.crud.DataSupport;

public class VotedID extends DataSupport {
    //当前用户已投的投票的id 实体内部类(用于存储在数据库)
    private int voted_id;
    public int getId() {
        return voted_id;
    }
    public void setId(int id) {
        this.voted_id = id;
    }
}
