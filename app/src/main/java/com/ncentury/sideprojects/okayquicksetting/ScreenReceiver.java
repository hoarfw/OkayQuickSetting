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
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)|| intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if(MODE.valueOf(SPUtil.get("CURRENT_MODE", "DUAL"))==MODE.READ){
                try {
                    Thread.sleep(1000);
                    RootCmd.execRootCmdSilent("setprop sys.close.mainTp 1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }



    }

}