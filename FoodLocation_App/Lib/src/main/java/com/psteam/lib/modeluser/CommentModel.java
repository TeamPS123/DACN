package com.psteam.lib.modeluser;

import java.io.Serializable;

public class CommentModel implements Serializable {
    private String date;

    private String name;

    private String imgUser;

    private String content;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentModel(String date, String name, String imgUser, String content) {
        this.date = date;
        this.name = name;
        this.imgUser = imgUser;
        this.content = content;
    }
}
