package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class GetUserReserveTableModel {
    private String notification;

    private ArrayList<ReserveTable> reserveTables;

    private String status;

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public ArrayList<ReserveTable> getReserveTables ()
    {
        return reserveTables;
    }

    public void setReserveTables (ArrayList<ReserveTable> reserveTables)
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
