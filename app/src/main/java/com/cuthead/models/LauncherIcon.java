package com.cuthead.models;

/**
 * Created by Jiaqi Ning on 2014/7/10.
 */
public class LauncherIcon {
    private String name;
    private int imgId;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getBottomBarColor() {
        return bottomBarColor;
    }

    private int backgroundColor;
    private int bottomBarColor;

    public LauncherIcon(String name,int imgId, int bgColor, int bottomColor){
        this.name = name;
        this.imgId = imgId;
        backgroundColor = bgColor;
        bottomBarColor = bottomColor;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }
}
