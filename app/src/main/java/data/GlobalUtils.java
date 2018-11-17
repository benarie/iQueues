package data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class GlobalUtils {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Globals.SHARED_PREF_NAME, Activity.MODE_PRIVATE);
    }

    public static void setStringToLocalStorage(Context context, String key, String val) {
        getSharedPreferences(context).edit().putString(key, val).apply();
    }

    public static String getStringFromLocalStorage(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static void removeStringFromLocalStorage(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).apply();
    }

    public static void cleanUserDataFromMemory(Context context) {
        removeStringFromLocalStorage(context, Globals.FULL_NAME_LOCAL_STORAGE_KEY);
        removeStringFromLocalStorage(context, Globals.UID_LOCAL_STORAGE_KEY);
    }

    public static Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static long convertDateToTimestamp(String dateOfOrder, String timeOfOrder) {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Date date = null;
        try {
            date = formatter.parse(dateOfOrder + " " + timeOfOrder);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }



}
