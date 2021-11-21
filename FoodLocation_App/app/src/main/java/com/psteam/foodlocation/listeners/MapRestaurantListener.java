package com.psteam.foodlocation.listeners;

import com.psteam.foodlocation.models.RestaurantModel;

public interface MapRestaurantListener {
    void onRestaurantGuideClicked(RestaurantModel restaurantModel);
    void onRestaurantClicked(RestaurantModel restaurantModel);
}
