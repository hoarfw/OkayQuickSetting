package com.ncentury.sideprojects.okayquicksetting;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ScreenReceiver extends BroadcastReceiver
{
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // do whatever you need to do here
                wasScreenOn = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // and do whatever you need to do here
                wasScreenOn = true;
                //Toast.makeText(context.getApplicationContext(), "Screen ON", Toast.LENGTH_SHORT).show();
                if (ScreenUtils.CURRENT_MODE == MODE.READ) {
                    //READ MODE
                    // Toast.makeText(context.getApplicationContext(), "READ MODE", Toast.LENGTH_SHORT).show();
                    try {
                        ScreenUtils.CloseMainDisplay();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}