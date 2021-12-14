package com.psteam.lib.modeluser;

import java.io.Serializable;

public class Rate implements Serializable
{
    private String date;

    private String id;

    private String restaurantId;

    private String userName;

    private String value;

    private String userId;

    private String content;

    private String imageUser;

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
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
}
