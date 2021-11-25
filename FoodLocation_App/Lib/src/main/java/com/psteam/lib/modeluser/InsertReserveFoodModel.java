package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class InsertReserveFoodModel {
    private String reserveTableId;

    private ArrayList<ReserveFood> foods;

    private String userId;

    public String getReserveTableId() {
        return reserveTableId;
    }

    public void setReserveTableId(String reserveTableId) {
        this.reserveTableId = reserveTableId;
    }

    public ArrayList<ReserveFood> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<ReserveFood> foods) {
        this.foods = foods;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}