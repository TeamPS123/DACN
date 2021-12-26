package com.psteam.foodlocation.ultilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.psteam.foodlocation.adapters.NotificationAdapter;
import com.psteam.lib.modeluser.RestaurantModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PreferenceManager {
    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putRestaurantModel(String key, RestaurantModel restaurantModel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurantModel);
        editor.putString(key, json);
        editor.commit();
    }

    public void AddRestaurant(RestaurantModel restaurantModel) {
        ArrayList<RestaurantModel> restaurantModels = getListRestaurantRecent(Constants.TAG_RESTAURANT_RECENT);
        if (restaurantModels == null) {
            restaurantModels = new ArrayList<>();
        }
        if (!CheckRes(restaurantModels, restaurantModel.getRestaurantId())) {
            restaurantModels.add(0, restaurantModel);
            if (restaurantModels.size() > 10) {
                restaurantModels.removeAll(restaurantModels.subList(10, restaurantModels.size()));
            }
        }
        putListRestaurant(Constants.TAG_RESTAURANT_RECENT, restaurantModels);
    }

    public void AddNotification(NotificationAdapter.Notification notification) {
        ArrayList<NotificationAdapter.Notification>  notifications= getListNotification(Constants.TAG_NOTIFICATION);
        if (notifications == null) {
            notifications = new ArrayList<>();
        }

        notifications.add(0, notification);
        putListNotification(Constants.TAG_NOTIFICATION, notifications);
    }

    public ArrayList<NotificationAdapter.Notification> getListNotification(String key) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<NotificationAdapter.Notification>>() {
        }.getType();
        ArrayList<NotificationAdapter.Notification> obj = gson.fromJson(json, type);
        return obj;
    }

    public void putListNotification(String key, ArrayList<NotificationAdapter.Notification> notifications) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notifications);
        editor.putString(key, json);
        editor.commit();
    }

    public boolean CheckRes(ArrayList<RestaurantModel> restaurantModels, String resId) {
        for (RestaurantModel x : restaurantModels) {
            if (x.getRestaurantId().equals(resId)) {
                return true;
            }
        }
        return false;
    }

    public void clearRes() {
        sharedPreferences.edit().remove(Constants.TAG_RESTAURANT_RECENT).commit();
    }

    public RestaurantModel getRestaurantModel(String key) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        RestaurantModel restaurantModel = gson.fromJson(json, RestaurantModel.class);
        return restaurantModel;
    }

    public void putListRestaurant(String key, ArrayList<RestaurantModel> restaurantModels) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurantModels);
        editor.putString(key, json);
        editor.commit();
    }

    public ArrayList<RestaurantModel> getListRestaurantRecent(String key) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<RestaurantModel>>() {
        }.getType();
        ArrayList<RestaurantModel> obj = gson.fromJson(json, type);
        return obj;
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
