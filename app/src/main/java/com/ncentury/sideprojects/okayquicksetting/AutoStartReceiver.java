package com.ncentury.sideprojects.okayquicksetting;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartReceiver extends BroadcastReceiver
{
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        paramIntent = new Intent(paramContext, MainActivity.class);
        paramIntent.putExtra("START_FROM_BOOT",true);
        paramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        paramContext.startActivity(paramIntent);

        //Intent serviceIntent = new Intent(paramContext, OkayQuickSettingForegroundService.class);
        //serviceIntent.setAction("ACTION_VIDEO_MODE");
        //paramContext.startService(serviceIntent);

    }
}