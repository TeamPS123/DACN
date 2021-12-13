package com.psteam.lib.Models.reserveTableDetail;

import java.util.ArrayList;

public class messageReserveTable {
    public messageReserveTable(int status, String notification, reserveTable reserveTable, ArrayList<food> foodList) {
        this.status = status;
        this.notification = notification;
        this.reserveTable = reserveTable;
        this.foodList = foodList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public reserveTable getReserveTable() {
        return reserveTable;
    }

    public void setReserveTable(reserveTable reserveTable) {
        this.reserveTable = reserveTable;
    }

    public ArrayList<food> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<food> foodList) {
        this.foodList = foodList;
    }

    private int status;
    private String notification;
    private reserveTable reserveTable;
    private ArrayList<food> foodList;
}
