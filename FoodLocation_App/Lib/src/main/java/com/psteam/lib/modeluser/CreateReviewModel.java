package com.psteam.lib.modeluser;

public class CreateReviewModel {
    private String date;

    private String restaurantId;

    private String value;

    private String userId;

    private String content;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CreateReviewModel(String date, String restaurantId, String value, String userId, String content) {
        this.date = date;
        this.restaurantId = restaurantId;
        this.value = value;
        this.userId = userId;
        this.content = content;
    }
}