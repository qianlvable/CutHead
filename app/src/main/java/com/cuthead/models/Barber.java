package com.cuthead.models;

/**
 * Created by asus on 2014/7/14.
 */
public class Barber {
    String name;
    String address;
    String phone;
    String time;
    String shop;
    int distance;

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Barber(){

    }
    public Barber(String name,String address,String phone,String time,int distance ){
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.time = time;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
