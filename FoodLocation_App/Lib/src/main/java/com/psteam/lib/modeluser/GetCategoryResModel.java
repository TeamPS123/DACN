package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class GetCategoryResModel {
    private String notification;
    private ArrayList<CategoryRes> categoryResList;
    private String status;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public ArrayList<CategoryRes> getCategoryResList() {
        return categoryResList;
    }

    public void setCategoryResList(ArrayList<CategoryRes> categoryResList) {
        this.categoryResList = categoryResList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
