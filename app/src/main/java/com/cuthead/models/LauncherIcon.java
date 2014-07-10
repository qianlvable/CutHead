package com.cuthead.models;

/**
 * Created by Jiaqi Ning on 2014/7/10.
 */
public class LauncherIcon {
    private String name;
    private int imgId;
    public LauncherIcon(String name,int imgId){
        this.name = name;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }
}
