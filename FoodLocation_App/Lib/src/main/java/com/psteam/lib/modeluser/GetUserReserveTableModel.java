package com.psteam.lib.modeluser;

import com.psteam.lib.Models.reserveTableDetail.reserveTable;

import java.util.ArrayList;

public class GetUserReserveTableModel {
    private String notification;

    private ArrayList<reserveTable> reserveTables;

    private String status;

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public ArrayList<reserveTable> getReserveTables ()
    {
        return reserveTables;
    }

    public void setReserveTables (ArrayList<reserveTable> reserveTables)
    {
        this.reserveTables = reserveTables;
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
