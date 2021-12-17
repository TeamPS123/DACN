package com.psteam.lib.modeluser;

import java.io.Serializable;
import java.util.ArrayList;

public class GetReviewRestaurantModel implements Serializable{
    private String notification;

    private ArrayList<ReviewModel> reviews;

    private String reviewTotal;

    private String status;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public ArrayList<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewModel> reviews) {
        this.reviews = reviews;
    }

    public String getReviewTotal() {
        return reviewTotal;
    }

    public void setReviewTotal(String reviewTotal) {
        this.reviewTotal = reviewTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
