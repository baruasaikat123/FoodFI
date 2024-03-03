package com.example.foodsi.ProjectClass;

import android.content.Context;
import android.os.Build;
import android.view.Window;

import androidx.core.content.ContextCompat;

public class StatusBar {
    public void setStatusBarColor(Window window, Context context, int colorCode){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(ContextCompat.getColor(context, colorCode));
        }
    }
}
