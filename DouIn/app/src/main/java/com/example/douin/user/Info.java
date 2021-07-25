package com.example.douin.user;

import com.example.douin.util.network.VideoItem;

import java.util.ArrayList;

/**
 * 全局信息保存类
 *
 * @author 方昊
 */
public class Info {
    static Info i =new Info();

    public String user_email="EMPTY";

    public ArrayList<VideoItem> videoItemArrayList;

    public int position;

    public boolean isused = false;

    private Info(){

    }
    static public Info getinstance(){
       return i;
    }
}
