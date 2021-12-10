package com.psteam.lib.Models.reserveTableDetail;

import java.io.Serializable;

public class reserveTable implements Serializable {
    public reserveTable(String id, int quantity, String time, String promotionId, String name, String phone, String note, int status, restaurant restaurant) {
        Id = id;
        this.quantity = quantity;
        this.time = time;
        this.promotionId = promotionId;
        this.name = name;
        this.phone = phone;
        this.note = note;
        this.status = status;
        this.restaurant = restaurant;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(restaurant restaurant) {
        this.restaurant = restaurant;
    }

    private String Id ;
    private int quantity ;
    private String time ;
    private String promotionId ;
    private String name ;
    private String phone ;
    private String note ;
    private int status ;
    private restaurant restaurant;
}
