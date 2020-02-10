package com.chen.data;

import java.io.Serializable;
import java.util.List;

//"创建投票"的本地缓存数据
public class VoteData implements Serializable {
    //用于本地缓存的key
    public static final String CONTENTS="contents";
    public static final String TITLE="title";
    //用于本地缓存的key

    private List<String> vote_contents;
    private String title;

    public VoteData(List<String> vote_contents,String title){
        this.vote_contents=vote_contents;
        this.title=title;
    }

    public List<String> getVote_contents() {
        return vote_contents;
    }
    public String getTitle() {
        return title;
    }

    /*public void setVote_contents(List<String> vote_contents) {
        this.vote_contents = vote_contents;
    }*/
    public void setTitle(String title) {
        this.title = title;
    }
}
