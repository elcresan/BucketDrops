package com.supersmiley.bucketdrops.extras;

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
}
