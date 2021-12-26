package com.psteam.lib.modeluser;

public class InputSuggestRes {
    private String distance;

    private String lon;

    private String userId;

    private String lat;

    private String rangeDay;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getRangeDay() {
        return rangeDay;
    }

    public void setRangeDay(String rangeDay) {
        this.rangeDay = rangeDay;
    }

    public InputSuggestRes(String distance, String lon, String userId, String lat, String rangeDay) {
        this.distance = distance;
        this.lon = lon;
        this.userId = userId;
        this.lat = lat;
        this.rangeDay = rangeDay;
    }
}