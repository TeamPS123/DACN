package com.psteam.foodlocation.listeners;

import com.psteam.foodlocation.models.RestaurantModel;

public interface MapRestaurantListener {
    void onRestaurantClicked(RestaurantModel restaurantModel);
}
