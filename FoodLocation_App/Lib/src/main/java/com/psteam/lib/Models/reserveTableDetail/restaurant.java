package com.psteam.lib.Models.reserveTableDetail;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class restaurant implements Serializable {
    public restaurant(String userId, String restaurantId, String name, String line, String city, String district, String longLat, String openTime, String closeTime, String distance, String phoneRes, String mainPic, boolean status, String statusCO, ArrayList<String> pic, String categoryResStr, ArrayList<promotion> promotionRes, ArrayList<categoryRes> categoryRes) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.name = name;
        this.line = line;
        this.city = city;
        this.district = district;
        this.longLat = longLat;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.distance = distance;
        this.phoneRes = phoneRes;
        this.mainPic = mainPic;
        this.status = status;
        this.statusCO = statusCO;
        this.pic = pic;
        this.categoryResStr = categoryResStr;
        this.promotionRes = promotionRes;
        this.categoryRes = categoryRes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLongLat() {
        return longLat;
    }

    public void setLongLat(String longLat) {
        this.longLat = longLat;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPhoneRes() {
        return phoneRes;
    }

    public void setPhoneRes(String phoneRes) {
        this.phoneRes = phoneRes;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusCO() {
        return statusCO;
    }

    public void setStatusCO(String statusCO) {
        this.statusCO = statusCO;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public String getCategoryResStr() {
        return categoryResStr;
    }

    public void setCategoryResStr(String categoryResStr) {
        this.categoryResStr = categoryResStr;
    }

    public ArrayList<promotion> getPromotionRes() {
        return promotionRes;
    }

    public void setPromotionRes(ArrayList<promotion> promotionRes) {
        this.promotionRes = promotionRes;
    }

    public ArrayList<categoryRes> getCategoryRes() {
        return categoryRes;
    }

    public void setCategoryRes(ArrayList<categoryRes> categoryRes) {
        this.categoryRes = categoryRes;
    }

    public LatLng getLatLng() {
        String[] tempArray = longLat.split(",");
        double longitude = Double.parseDouble(tempArray[1]);
        double latitude = Double.parseDouble(tempArray[0]);
        return new LatLng(latitude, longitude);
    }

    public String getAddress(){
        return String.format("%s, %s, %s",line,district,city);
    }

    private String userId ;
    private String restaurantId ;
    private String name ;
    private String line ;
    private String city ;
    private String district ;
    private String longLat ;
    private String openTime ;
    private String closeTime ;
    private String distance ;
    private String phoneRes ;
    private String mainPic ;
    private boolean status ;
    private String statusCO ;
    private ArrayList<String> pic ;
    private String categoryResStr ;
    private ArrayList<promotion> promotionRes ;
    private ArrayList<categoryRes> categoryRes ;
}
