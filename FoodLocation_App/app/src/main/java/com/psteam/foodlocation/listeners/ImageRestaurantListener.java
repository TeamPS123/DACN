package com.psteam.foodlocation.listeners;

import android.graphics.Bitmap;
import android.view.View;

public interface ImageRestaurantListener {
    void onLongClick(View view, int position, Bitmap bitmap);
}
