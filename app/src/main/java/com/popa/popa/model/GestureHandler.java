package com.popa.popa.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class GestureHandler {

    private static GestureHandler instance;
    Intent intent = new Intent();

    public GestureHandler() {
        if (instance == null) {
            instance = new GestureHandler();
        }
    }


    public void swipeRight(Context context, Class <?extends Activity> activity){
        //intent = new Intent(v.getContext(), StatisticsActivity.class);
        //startActivity(intent);

    }

    public void swipeLeft(Context context, Class <?extends Activity> activity){
        //intent = new Intent(v.getContext(), StatisticsActivity.class);
        //startActivity(intent);
    }

    public static GestureHandler getInstance(){
        return instance;
    }
}
