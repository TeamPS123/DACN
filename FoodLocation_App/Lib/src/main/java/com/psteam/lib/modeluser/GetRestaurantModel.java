package com.psteam.lib.modeluser;

import java.io.Serializable;
import java.util.ArrayList;

public class GetRestaurantModel implements Serializable
{
    private ArrayList<String> districtList;

    private String notification;

    private ArrayList<RestaurantModel> resList;

    private ArrayList<CategoryRes> categoryList;

    private String status;

    public ArrayList<String> getDistrictList ()
    {
        return districtList;
    }

    public void setDistrictList (ArrayList<String> districtList)
    {
        this.districtList = districtList;
    }

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public ArrayList<RestaurantModel> getResList ()
    {
        return resList;
    }

    public void setResList (ArrayList<RestaurantModel> resList)
    {
        this.resList = resList;
    }

    public ArrayList<CategoryRes> getCategoryList ()
    {
        return categoryList;
    }

    public void setCategoryList (ArrayList<CategoryRes> categoryList)
    {
        this.categoryList = categoryList;
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