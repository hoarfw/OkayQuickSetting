package com.ncentury.sideprojects.okayquicksetting;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ScreenReceiver extends BroadcastReceiver
{
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            //Log.d("ScreenReceiver", "Screen OFF");
            //Toast.makeText(context.getApplicationContext(), "Screen OFF", Toast.LENGTH_SHORT).show();
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            wasScreenOn = true;
            Log.d("ScreenReceiver", "Screen ON");
            //Toast.makeText(context.getApplicationContext(), "Screen ON", Toast.LENGTH_SHORT).show();
            if (ScreenUtils.CURRENT_MODE == MODE.READ) {
                //READ MODE
                Log.d("ScreenReceiver", "Read Mode: Close main display after screen on");
                //Toast.makeText(context.getApplicationContext(), "READ MODE", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                    RootCmd.execRootCmdSilent("setprop sys.close.mainTp 1");
                    //ScreenUtils.CloseMainDisplay();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}