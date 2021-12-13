package com.psteam.lib.modeluser;

public class GetRestaurantByDistance {
    private String distance;
    private String lon;
    private String lat;

    public GetRestaurantByDistance(String distance, String lon, String lat) {
        this.distance = distance;
        this.lon = lon;
        this.lat = lat;
    }

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
