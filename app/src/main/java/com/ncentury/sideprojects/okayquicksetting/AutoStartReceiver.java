package com.ncentury.sideprojects.okayquicksetting;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartReceiver extends BroadcastReceiver
{
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        paramIntent = new Intent(paramContext, MainActivity.class);
        paramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        paramContext.startActivity(paramIntent);
    }
}