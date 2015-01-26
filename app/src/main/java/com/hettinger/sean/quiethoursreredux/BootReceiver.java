package com.hettinger.sean.quiethoursreredux;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class BootReceiver extends BroadcastReceiver {


    public BootReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BootReceiver", "Running");
        Intent startBackgroundCheck = new Intent("com.hettinger.sean.quiethoursreredux.BackgroundCheck");
        startBackgroundCheck.setClass(context, BackgroundCheck.class);
        context.startService(startBackgroundCheck);
    }
}