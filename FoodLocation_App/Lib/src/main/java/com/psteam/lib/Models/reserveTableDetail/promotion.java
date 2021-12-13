package com.psteam.lib.Models.reserveTableDetail;

import java.io.Serializable;

public class promotion implements Serializable {
    public promotion(String id, String name, String info, String value) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String id ;
    private String name ;
    private String info ;
    private String value ;
}
