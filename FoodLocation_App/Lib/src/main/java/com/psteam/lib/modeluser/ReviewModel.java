package com.psteam.lib.modeluser;

import java.io.Serializable;
import java.util.ArrayList;

public class ReviewModel implements Serializable {
    private String date;

    private String countLike;

    private String id;

    private String restaurantId;

    private String userName;

    private String value;

    private String userId;

    private String content;

    private String imageUser;

    private ArrayList<String> imgList;

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public String getCountLike ()
    {
        return countLike;
    }

    public void setCountLike (String countLike)
    {
        this.countLike = countLike;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getRestaurantId ()
    {
        return restaurantId;
    }

    public void setRestaurantId (String restaurantId)
    {
        this.restaurantId = restaurantId;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    public String getImageUser ()
    {
        return imageUser;
    }

    public void setImageUser (String imageUser)
    {
        this.imageUser = imageUser;
    }

    public ArrayList<String> getImgList ()
    {
        return imgList;
    }

    public void setImgList (ArrayList<String> imgList)
    {
        this.imgList = imgList;
    }

    public ReviewModel(String date, String countLike, String id, String restaurantId, String userName, String value, String userId, String content, String imageUser, ArrayList<String> imagePic) {
        this.date = date;
        this.countLike = countLike;
        this.id = id;
        this.restaurantId = restaurantId;
        this.userName = userName;
        this.value = value;
        this.userId = userId;
        this.content = content;
        this.imageUser = imageUser;
        this.imgList = imagePic;
    }
}
