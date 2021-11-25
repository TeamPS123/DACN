package com.psteam.foodlocation.ultilities;

import android.content.Context;
import android.content.SharedPreferences;

public class Token {
    private final Context context;
    public final static String expired ="expired";

    public Token(Context context) {
        this.context = context;
    }

    public void saveToken(String token) {

        SharedPreferences settings = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", "Bearer " +  token);
        editor.putLong("expires", System.currentTimeMillis() + 86400000); //24 * 3600000
        editor.apply();
    }

    public String getToken() {
        SharedPreferences settings = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        long expires = settings.getLong("expires", 0);
        if (expires < System.currentTimeMillis()) {
            return expired;
        }
        return settings.getString("token", "");
    }
}
