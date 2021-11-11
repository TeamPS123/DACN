package com.psteam.foodlocation.ultilities;

public class Para {
    public static double latitude = 0;
    public static double longitude = 0;
    public static String currentAddress = "";

    public static String currentLatLng() {
        return String.format("%s, %s", latitude, longitude);
    }
}
