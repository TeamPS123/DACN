package com.psteam.lib.modeluser;

public class GetInfoUser {
    private String notification;

    private UserModel user;

    private String status;

    public GetInfoUser(String notification, UserModel user, String status) {
        this.notification = notification;
        this.user = user;
        this.status = status;
    }

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public UserModel getUser ()
    {
        return user;
    }

    public void setUser (UserModel user)
    {
        this.user = user;
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
