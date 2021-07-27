package com.ncentury.sideprojects.okayquicksetting;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ButtonRemapService extends AccessibilityService {
    private static final String TAG = "ButtonRemapService";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_BTNMAP_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_BTNMAP_SERVICE";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        int keycode = event.getKeyCode();
        int action = event.getAction();
        Log.d(TAG,event.toString());

        if ((action != KeyEvent.ACTION_UP && action != KeyEvent.ACTION_DOWN) || event.isCanceled()) {
            return false;
        }

        if(DeviceUtils.isOkayS4()) {
            if (action == KeyEvent.ACTION_DOWN && (keycode == KeyEvent.KEYCODE_BACK || keycode == KeyEvent.KEYCODE_HOME || keycode == 521)) {
                // Don't want to send ACTION_DOWN to the apps as they may interpret it as a button
                // press that's held down.
                return true;
            }
            //ScreenUtils.CURRENT_MODE==MODE.READ &&

            if (keycode == KeyEvent.KEYCODE_BACK) {
                //PageDown();
                return false;
            } else if (keycode == KeyEvent.KEYCODE_HOME) {
               //PageUp();
                return false;
            } else if (keycode == 521) {
                PageDown();
                return true;
            }
        }else if(DeviceUtils.isOkayS4Plus()){

        }
        return false;
    }

    public void PageDown() {
        Log.d(TAG,"Simulate PAGE DOWN");
        RootCmd.execRootCmdSilent("input keyevent 93");
    }

    public void PageUp() {
        Log.d(TAG,"Simulate PAGE UP");
        RootCmd.execRootCmdSilent("input keyevent 92");
    }
    public void RefreshEinkDisplay() {
        Log.d(TAG,"RUN setprop sys.eink.reconfigure 1");
        //RootCmd.execRootCmdSilent("setprop sys.eink.reconfigure 1");
    }

    @Override
    public void onInterrupt() {
    }



}