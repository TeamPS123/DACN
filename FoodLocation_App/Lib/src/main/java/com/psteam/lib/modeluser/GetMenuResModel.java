package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class GetMenuResModel
{
    private String notification;

    private ArrayList<MenuModel> menuList;

    private String status;

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public ArrayList<MenuModel> getMenuList ()
    {
        return menuList;
    }

    public void setMenuList (ArrayList<MenuModel> menuList)
    {
        this.menuList = menuList;
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
