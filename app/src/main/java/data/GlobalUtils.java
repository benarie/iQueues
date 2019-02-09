package data;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.iQueues.DriverMainActivity;
import com.iQueues.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


/**
 * The type Global utils.
 */
public class GlobalUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Globals.SHARED_PREF_NAME, Activity.MODE_PRIVATE);
    }

    /**
     * Sets string to local storage.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    public static void setStringToLocalStorage(Context context, String key, String val) {
        getSharedPreferences(context).edit().putString(key, val).apply();
    }

    /**
     * Gets string from local storage.
     *
     * @param context the context
     * @param key     the key
     * @return the string from local storage
     */
    public static String getStringFromLocalStorage(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    private static void removeStringFromLocalStorage(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).apply();
    }

    /**
     * Clean user data from memory.
     *
     * @param context the context
     */
    public static void cleanUserDataFromMemory(Context context) {
        removeStringFromLocalStorage(context, Globals.FULL_NAME_LOCAL_STORAGE_KEY);
        removeStringFromLocalStorage(context, Globals.UID_LOCAL_STORAGE_KEY);
    }

    /**
     * Gets time stamp.
     *
     * @return the time stamp
     */
    public static Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * Convert date to timestamp long.
     *
     * @param dateOfOrder the date of order
     * @param timeOfOrder the time of order
     * @return the date in timestamp
     */
    public static long convertDateToTimestamp(String dateOfOrder, String timeOfOrder) {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Date date = null;
        try {
            date = formatter.parse(dateOfOrder + " " + timeOfOrder);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (date != null ? date.getTime() : 0);
    }

    /**
     * Show notification.
     *
     * @param title   the title
     * @param content the content
     * @param context the context
     */
    public static void showNotification(String title, String content, Context context) {
        Intent notificationIntent = new Intent(context, DriverMainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationManagerCompat.from(context).notify(new Random().nextInt(Integer.MAX_VALUE), generateNotification(title, content, intent, context));
    }

    /**
     * generateNotification
     *
     * @param title
     * @param content
     * @param intent
     * @param context
     * @return mBuilder.build() and build notification
     */
    private static Notification generateNotification(String title, String content, PendingIntent intent, Context context) {
        GlobalUtils.initChannels(context);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, Globals.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                .setBadgeIconType(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(new long[]{1000, 1000})
                .setContentIntent(intent)

                .setPriority(NotificationCompat.PRIORITY_MAX);
        return mBuilder.build();
    }

    /**
     * in android 8 need to add Channels to create notification
     * @param context
     */
    private static void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(Globals.NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(context.getString(R.string.app_name) + " Channel");
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setShowBadge(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

}
