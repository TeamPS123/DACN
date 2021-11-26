package com.psteam.lib.modeluser;

import java.io.Serializable;

public class ReserveTable implements Serializable {
    private String note;

    private String quantity;

    private String phone;

    private RestaurantModel restaurant;

    private String name;

    private String id;

    private String time;

    private String promotionId;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote ()
    {
        return note;
    }

    public void setNote (String note)
    {
        this.note = note;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public RestaurantModel getRestaurant ()
    {
        return restaurant;
    }

    public void setRestaurant (RestaurantModel restaurant)
    {
        this.restaurant = restaurant;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getPromotionId ()
    {
        return promotionId;
    }

    public void setPromotionId (String promotionId)
    {
        this.promotionId = promotionId;
    }
}
