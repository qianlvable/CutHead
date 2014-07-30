package com.cuthead.models;

/**
 * Created by Jiaqi Ning on 2014/7/30.
 */
public class OrderAccept {
    String orderID;
    String phone;
    String baber;
    double distance;
    String address;
    String time;
    String shop;

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBaber() {
        return baber;
    }

    public void setBaber(String baber) {
        this.baber = baber;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
