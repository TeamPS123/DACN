package com.psteam.lib.modeluser;

public class GetReserveTableInput {

    private String userId;
    private double lon;
    private double lat;

    public GetReserveTableInput(String userId, double lon, double lat) {
        this.userId = userId;
        this.lon = lon;
        this.lat = lat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
