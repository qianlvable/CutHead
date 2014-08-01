package com.cuthead.controller;

import android.util.Log;

import com.cuthead.models.MyTimeMark;

import java.util.ArrayList;

/**
 * Created by Jiaqi Ning on 2014/7/31.
 */
public class TimeUtil {

    public static class TimeGroup{
        String startTime;

        public String getEndTime() {
            return endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        String endTime;
        public TimeGroup(String start,String end){
            startTime = start;
            endTime = end;
        }

        int getStartHour(){
            String[] parts = startTime.split(":");
            return Integer.parseInt(parts[0]);
        }

        int getStartMin(){
            String[] parts = startTime.split(":");
            return Integer.parseInt(parts[1]);
        }
        int getEndHour(){
            String[] parts = endTime.split(":");
            return Integer.parseInt(parts[0]);
        }

        int getEndMin(){
            String[] parts = endTime.split(":");
            return Integer.parseInt(parts[1]);
        }
    }


    public static ArrayList<TimeGroup> pharseTimeString(String str){
        String[] timeNode = str.split("-");

        ArrayList<TimeGroup> timeList = new ArrayList<TimeGroup>();
        for (int i = 0; i < timeNode.length-1;i+= 2){
            int j = i+1;
            TimeGroup tg = new TimeGroup(timeNode[i],timeNode[j]);
            timeList.add(tg);
        }


        return timeList;
    }


    /** return MyTimeMark to control the number picker*/
    public static ArrayList<MyTimeMark> getAvailableTime(ArrayList<TimeGroup> timeGroups){
            ArrayList<MyTimeMark> result = new ArrayList<MyTimeMark>();
            for (TimeGroup group:timeGroups){
                for (int i = group.getStartHour();i <= group.getEndHour();i++){
                    if (i == group.getStartHour()){
                        switch (group.getStartMin()){
                            case 0:
                                result.add(new MyTimeMark(i,1,1,1));
                                break;
                            case 20:
                                result.add(new MyTimeMark(i,0,1,1));
                                break;
                            case 40:
                                result.add(new MyTimeMark(i,0,0,1));
                                break;
                        }
                        continue;

                    }

                    if (i == group.getEndHour()){
                        switch (group.getEndMin()){
                            case 0:
                                result.add(new MyTimeMark(i,1,0,0));
                                break;
                            case 20:
                                result.add(new MyTimeMark(i,1,1,0));
                                break;
                            case 40:
                                result.add(new MyTimeMark(i,1,1,1));
                                break;
                        }
                    }

                    else{
                        result.add(new MyTimeMark(i,1,1,1));
                    }

                }
            }
        return result;
    }

}
