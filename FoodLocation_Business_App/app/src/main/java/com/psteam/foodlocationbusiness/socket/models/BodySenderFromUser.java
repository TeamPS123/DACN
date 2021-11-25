package com.psteam.foodlocationbusiness.socket.models;

public class BodySenderFromUser {
    public BodySenderFromUser(String reserveTableId, int quantity, String time, String restaurantId, String name, String phone, String promotionId) {
        this.reserveTableId = reserveTableId;
        this.quantity = quantity;
        this.time = time;
        this.restaurantId = restaurantId;
        this.name = name;
        this.phone = phone;
        this.promotionId = promotionId;
    }

    public String getReserveTableId() {
        return reserveTableId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTime() {
        return time;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPromotionId() {
        return promotionId;
    }

    private String reserveTableId;
    private int quantity;
    private String time;
    private String restaurantId;
    private String name;
    private String phone;
    private String promotionId;
}
