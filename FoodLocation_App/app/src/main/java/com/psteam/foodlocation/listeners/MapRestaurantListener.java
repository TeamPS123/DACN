package com.psteam.foodlocation.listeners;


import com.psteam.lib.modeluser.RestaurantModel;

public interface MapRestaurantListener {
    void onRestaurantGuideClicked(RestaurantModel restaurantModel);
    void onRestaurantClicked(RestaurantModel restaurantModel);
}
