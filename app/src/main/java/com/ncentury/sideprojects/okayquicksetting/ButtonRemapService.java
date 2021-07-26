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
    private static final String BACK = "5";
    private static final String HOME = "4";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_BTNMAP_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_BTNMAP_SERVICE";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        int keycode = event.getKeyCode();
        int action = event.getAction();
        Toast.makeText(getApplicationContext(),keycode,Toast.LENGTH_SHORT).show();
        if (action != KeyEvent.ACTION_UP && action != KeyEvent.ACTION_DOWN || event.isCanceled()) {
            return false;
        }
        if(ScreenUtils.CURRENT_MODE!=MODE.READ)
            return true;
        //if (action == KeyEvent.ACTION_DOWN && (keycode == KeyEvent.KEYCODE_BACK || keycode == KeyEvent.KEYCODE_HOME)) {
        if (action == KeyEvent.ACTION_DOWN && (keycode == KeyEvent.KEYCODE_BACK)) {
            // Don't want to send ACTION_DOWN to the apps as they may interpret it as a button
            // press that's held down.
            return true;
        }

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                PageDown();
                break;
            case KeyEvent.KEYCODE_HOME:
                //PageUp();
                break;
        }

        return false;
    }

    public void PageDown() {
        RootCmd.execRootCmdSilent("input keyevent 93");
    }

    public void PageUp() {
        RootCmd.execRootCmdSilent("input keyevent 92");
    }

    @Override
    public void onInterrupt() {
    }



}