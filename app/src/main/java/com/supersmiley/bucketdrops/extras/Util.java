package com.supersmiley.bucketdrops.extras;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;

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
}