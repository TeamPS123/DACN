package com.psteam.lib.modeluser;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class RestaurantModel implements Serializable {
    private String phoneRes;

    private ArrayList<PromotionModel> promotionRes;

    private String distance;

    private String city;

    private String line;

    private String categoryResStr;

    private String longLat;

    private ArrayList<CategoryRes> categoryRes;

    private ArrayList<String> pic;

    private String restaurantId;

    private String district;

    private String name;

    private String closeTime;

    private String mainPic;

    private String openTime;

    public String getPhoneRes() {
        return phoneRes;
    }

    public void setPhoneRes(String phoneRes) {
        this.phoneRes = phoneRes;
    }

    public ArrayList<PromotionModel> getPromotionRes() {
        return promotionRes;
    }

    public void setPromotionRes(ArrayList<PromotionModel> promotionRes) {
        this.promotionRes = promotionRes;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCategoryResStr() {
        return categoryResStr;
    }

    public void setCategoryResStr(String categoryResStr) {
        this.categoryResStr = categoryResStr;
    }

    public String getLongLat() {
        return longLat;
    }

    public void setLongLat(String longLat) {
        this.longLat = longLat;
    }

    public ArrayList<CategoryRes> getCategoryRes() {
        return categoryRes;
    }

    public void setCategoryRes(ArrayList<CategoryRes> categoryRes) {
        this.categoryRes = categoryRes;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        String[] tempArray = longLat.split(",");
        double longitude = Double.parseDouble(tempArray[0]);
        double latitude = Double.parseDouble(tempArray[1]);
        return new LatLng(latitude, longitude);
    }

    public String getAddress(){
        return String.format("%s, %s, %s",line,district,city);
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}
