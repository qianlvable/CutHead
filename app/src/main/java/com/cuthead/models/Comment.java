package com.cuthead.models;

/**
 * Created by zsx on 2015/2/23.
 */
public class Comment {
    int profile;
    int rank;
    String name;
    String content;
    String time;

    public Comment(int profile,int rank,String name,String content,String time){
        this.profile = profile;
        this.rank = rank;
        this.name = name;
        this.content = content;
        this.time = time;
    }

    public int getProfile() {
        return profile;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }
}
