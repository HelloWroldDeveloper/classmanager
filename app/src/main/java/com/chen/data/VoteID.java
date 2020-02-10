package com.chen.data;

import org.litepal.crud.DataSupport;

public class VoteID extends DataSupport {
    //当前用户创建的投票的id 实体内部类(用于存储在数据库)
    private int vote_id;
    public int getId() {
        return vote_id;
    }
    public void setId(int id) {
        this.vote_id = id;
    }
}
