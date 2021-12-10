package com.psteam.lib.modeluser;


import java.util.ArrayList;

public class GetReserveTableSinge {
    public GetReserveTableSinge(String notification, ArrayList<FoodModel> foodList, ReserveTable reserveTable, String status) {
        this.notification = notification;
        this.foodList = foodList;
        this.reserveTable = reserveTable;
        this.status = status;
    }

    private String notification;
    private ArrayList<FoodModel> foodList;
    private ReserveTable reserveTable;
    private String status;

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public ArrayList<FoodModel> getFoodList ()
    {
        return foodList;
    }

    public void setFoodList (ArrayList<FoodModel> foodList)
    {
        this.foodList = foodList;
    }

    public ReserveTable getReserveTable ()
    {
        return reserveTable;
    }

    public void setReserveTable (ReserveTable reserveTable)
    {
        this.reserveTable = reserveTable;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}
