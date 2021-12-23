package com.psteam.lib.modeluser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RestaurantModel implements Serializable, ClusterItem {
    private String phoneRes;

    private ArrayList<PromotionModel> promotionRes;

    private String userId;

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

    private boolean status;

    private String statusCO;

    private String rateTotal;

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
        double longitude = Double.parseDouble(tempArray[1]);
        double latitude = Double.parseDouble(tempArray[0]);
        return new LatLng(latitude, longitude);
    }

    public String getPromotion() {
        return (promotionRes != null && promotionRes.size() > 0) ? String.format("-%s%%", promotionRes.get(0).getValue()) : "Đặt bàn";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return String.format("%s, %s, %s", line, district, city);
    }

    public String getCloseTime() {
        String s = new SimpleDateFormat("a").format(new Date());
        if (s.equals("AM") || s.equals("PM")) {
            if (closeTime.contains("SA")) {
                return closeTime.replace("SA", "AM");
            } else if (closeTime.contains("CH")) {
                return closeTime.replace("CH", "PM");
            } else {
                return closeTime;
            }
        } else {
            return closeTime;
        }
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
        String s = new SimpleDateFormat("a").format(new Date());
        if (s.equals("AM") || s.equals("PM")) {
            if (openTime.contains("SA")) {
                return openTime.replace("SA", "AM");
            } else if (openTime.contains("CH")) {
                return openTime.replace("CH", "PM");
            } else {
                return openTime;
            }
        } else {
            return openTime;
        }
    }

    public static Date coverStringToDate(String strDate) {
        try {
            Date date = new SimpleDateFormat("hh:mm a dd/MM/yyyy").parse(strDate);
            return date;
        } catch (ParseException e) {
            return new Date();
        }
    }

    public Date getStatusOpen() {
        return coverStringToDate(String.format("%s %s", getOpenTime(), statusCO));
    }

    public Date getStatusClose() {
        return coverStringToDate(String.format("%s %s", getCloseTime(), statusCO));
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getStatusCO() {
        return statusCO;
    }

    public void setStatusCO(String statusCO) {
        this.statusCO = statusCO;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRateTotal() {
        return rateTotal;
    }

    public void setRateTotal(String rateTotal) {
        this.rateTotal = rateTotal;
    }
/* public double getLatitude() {
        String[] latLng_Array = getLatLng().split(",");
        return Double.parseDouble(latLng_Array[0]);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LatLng LatLng() {
        String[] latLng_Array = getLatLng().split(",");
        LatLng latLng = new LatLng(Double.parseDouble(latLng_Array[0]), Double.parseDouble(latLng_Array[1]));
        return latLng;
    }

    public double getLongitude() {
        String[] latLng_Array = getLatLng().split(",");
        return Double.parseDouble(latLng_Array[1]);
    }*/

    @Override
    public String toString() {
        return "RestaurantModel{" +
                "name='" + name + '\'' +
                '}';
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return getLatLng();
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }
}
