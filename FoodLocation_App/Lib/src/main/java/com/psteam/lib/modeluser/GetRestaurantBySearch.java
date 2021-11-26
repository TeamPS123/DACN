package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class GetRestaurantBySearch {
    private ArrayList<String> districtList;

    private ArrayList<String> catelogyList;

    private String name;

    private String lon;

    private String lat;

    public ArrayList<String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(ArrayList<String> districtList) {
        this.districtList = districtList;
    }

    public ArrayList<String> getCatelogyList() {
        return catelogyList;
    }

    public void setCatelogyList(ArrayList<String> catelogyList) {
        this.catelogyList = catelogyList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public GetRestaurantBySearch(ArrayList<String> districtList, ArrayList<String> catelogyList, String name, String lon, String lat) {
        this.districtList = districtList;
        this.catelogyList = catelogyList;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }
}
