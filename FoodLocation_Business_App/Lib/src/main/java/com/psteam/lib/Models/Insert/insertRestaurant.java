package com.psteam.lib.Models.Insert;

public class insertRestaurant {
    public insertRestaurant(String name, String userId, String line, String city, String district, String longLat, String openTime, String closeTime) {
        this.name = name;
        this.userId = userId;
        this.line = line;
        this.city = city;
        this.district = district;
        this.longLat = longLat;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getLine() {
        return line;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getLongLat() {
        return longLat;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    private String name ;
    private String userId ;
    private String line ;
    private String city ;
    private String district ;
    private String longLat ;
    private String openTime ;
    private String closeTime ;
}
