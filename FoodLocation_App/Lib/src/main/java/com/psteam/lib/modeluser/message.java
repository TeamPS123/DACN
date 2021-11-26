package com.psteam.lib.modeluser;

public class message {
    private String notification;
    private String id;
    private String status;

    public message(String notification, String id, String status) {
        this.notification = notification;
        this.id = id;
        this.status = status;
    }

    public message(String notification, String status) {
        this.notification = notification;
        this.status = status;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}