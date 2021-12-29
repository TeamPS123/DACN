package com.psteam.foodlocation.ultilities;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constants {
    public static final String KEY_PREFERENCE_NAME = "FoodLocationPreference";
    public static final String PACKAGE_NAME = "com.psteam.foodlocation";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_CITY = PACKAGE_NAME + ".RESULT_CITY";
    public static final String RESULT_SUB_CITY = PACKAGE_NAME + ".RESULT_SUB_CITY";
    public static final String RESULT_SUB_ADMIN_CITY = PACKAGE_NAME + ".RESULT_SUB_ADMIN_CITY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final int SUCCESS_RESULT = 1;
    public static final int FAILURE_RESULT = 0;
    public static final String TAG_RESTAURANT = "tag_restaurant";

    public static final int RADIUS = 5000;

    public static final int LOCATION_SERVICE_ID = 175;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";

    public static final int TAB_POSITION_PENDING = 0;
    public static final int TAB_POSITION_PROCESSING = 1;
    public static final int TAB_POSITION_CONFIRMED = 2;
    public static final int TAB_POSITION_LATE = 3;

    public static final int FLAG_CHANGE_PASSWORD = 1;
    public static final int FLAG_UPDATE_USER_INFO = 2;

    public static final String USER_ID = "userId";
    public static final String IsLogin = "IsLogin";
    public static final String Password = "Password";

    public static final String EXPIRES = "expires";
    public static final String TAG_RESTAURANT_RECENT = "TAG_RESTAURANT_RECENT";
    public static final String TAG_NOTIFICATION = "TAG_NOTIFICATION";
    public static final String TAG_NOTIFICATION_SUCCESS = "TAG_NOTIFICATION";
    public static final String TAG_DISTANCE = "TAG_DISTANCE";
    public static final String TAG_DAY_RECOMMEND = "TAG_DAY_RECOMMEND";

    public static Date coverStringToDate(String strDate) {
        try {
            Date date = new SimpleDateFormat("hh:mm a,yyyy-MM-dd", new Locale("vi","VN")).parse(strDate);
            return date;
        } catch (ParseException e) {
            try {
               return new SimpleDateFormat("hh:mm a,yyyy-MM-dd",Locale.US).parse(strDate);
            }catch (ParseException ex){
                return new Date();
            }
        }
    }

    public static String formatToYesterdayOrToday(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, 1);
        DateFormat timeFormatter = new SimpleDateFormat("dd MMM yyyy");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return new SimpleDateFormat("hh:mm, Hôm nay dd MMM yyyyy").format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return new SimpleDateFormat("hh:mm, Ngày mai dd MMM yyyyy").format(dateTime);
        } else {
            return new SimpleDateFormat("hh:mm, EEEE dd MMM yyyy").format(dateTime);
        }
    }

}
