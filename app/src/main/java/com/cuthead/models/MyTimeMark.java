package com.cuthead.models;

/**
 * Created by Jiaqi Ning on 2014/7/31.
 */
public class MyTimeMark {
    int hour;
    int ZeroMark;       //stand for 00 whether exist
    int TwentyMark;     //stand for 20 whether exist
    int FourtyMark;    //stand for 40 whether exist

    public MyTimeMark(int h, int z, int t, int f){
        hour = h;
        ZeroMark = z;
        TwentyMark = t;
        FourtyMark = f;
    }
    public int getHour()
    {
        return hour;
    }
    public int getZeroMark()
    {
        return ZeroMark;
    }
    public int getTwentyMark()
    {
        return TwentyMark;
    }
    public int getFourtyMark()
    {
        return FourtyMark;
    }

    @Override
    public String toString() {
        return hour+" "+ZeroMark+" "+TwentyMark+" "+FourtyMark;
    }
}
