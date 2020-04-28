package com.arturdevmob.tasklist.utils;

import android.util.Log;

import java.text.SimpleDateFormat;

public class Utils {
    public static void d(Object obj) {
        Log.d("MY_TEST", String.valueOf(obj));
    }

    public static String getFormattedDateString(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy в H:mm");
        return dateFormat.format(time);
    }

    public static String trimString(String str) {
        if (str.length() > 90) {
            return String.format("%s ...", str.substring(0, 90));
        }

        return str;
    }
}
