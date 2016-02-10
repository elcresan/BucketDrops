package com.supersmiley.bucketdrops.extras;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

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
}