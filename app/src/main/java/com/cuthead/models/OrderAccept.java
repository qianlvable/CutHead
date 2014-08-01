package com.cuthead.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jiaqi Ning on 2014/7/30.
 */
public class OrderAccept implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.phone);
        dest.writeString(this.baber);
        dest.writeDouble(this.distance);
        dest.writeString(this.address);
        dest.writeString(this.time);
        dest.writeString(this.shop);
    }

    public OrderAccept() {
    }

    private OrderAccept(Parcel in) {
        this.orderID = in.readString();
        this.phone = in.readString();
        this.baber = in.readString();
        this.distance = in.readDouble();
        this.address = in.readString();
        this.time = in.readString();
        this.shop = in.readString();
    }

    public static final Parcelable.Creator<OrderAccept> CREATOR = new Parcelable.Creator<OrderAccept>() {
        public OrderAccept createFromParcel(Parcel source) {
            return new OrderAccept(source);
        }

        public OrderAccept[] newArray(int size) {
            return new OrderAccept[size];
        }
    };
}
