package com.supersmiley.bucketdrops.extras;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;

import com.supersmiley.bucketdrops.services.NotificationService;

import java.util.Calendar;
import java.util.List;

public class Util {

    // Set the visibility as visible to all the views.
    public static void showViews(List<View> views){
        for(View view : views){
            view.setVisibility(View.VISIBLE);
        }
    }

    // Set the visibility as gone to all the views.
    public static void hideViews(List<View> views){
        for(View view : views){
            view.setVisibility(View.GONE);
        }
    }

    // Check android version is more than jellybean.
    public static boolean moreThanJellyBean(){
        return Build.VERSION.SDK_INT > 15;
    }

    // Set background using the proper method for the android version.
    public static void setBackground(View mItemView, Drawable drawable) {
        if(moreThanJellyBean()) {
            mItemView.setBackground(drawable);
        } else {
            mItemView.setBackgroundDrawable(drawable);
        }
    }

    public static Calendar getCalendarFromDatePicker(DatePicker date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        calendar.set(Calendar.MONTH, date.getMonth());
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public static void scheduleAlarm(Context context){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 3600000, pendingIntent);
    }
}