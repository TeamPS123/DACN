package com.psteam.lib.modeluser;

public class SearchCheckInModel {
    private String key;
    private String lon;
    private String lat;


    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public SearchCheckInModel(String key, String lon, String lat) {
        this.key = key;
        this.lon = lon;
        this.lat = lat;
    }
}
