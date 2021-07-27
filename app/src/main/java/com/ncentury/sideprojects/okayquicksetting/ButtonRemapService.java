package com.ncentury.sideprojects.okayquicksetting;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ButtonRemapService extends AccessibilityService {
    private static final String TAG = "ButtonRemapService";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_BTNMAP_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_BTNMAP_SERVICE";
    private static String CurrentActivity="";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        event.getClassName().toString()
                );

                ActivityInfo activityInfo = tryGetActivity(componentName);
                boolean isActivity = activityInfo != null;
                if (isActivity) {
                    CurrentActivity=componentName.flattenToShortString();
                    Log.i("CurrentActivity", CurrentActivity);
                }
            }
        }
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        int keycode = event.getKeyCode();
        int action = event.getAction();
        Log.d(TAG,event.toString());

        if ((action != KeyEvent.ACTION_UP && action != KeyEvent.ACTION_DOWN) || event.isCanceled()) {
            return false;
        }
        String pkg=GetCurrentRunningPackage();
        Log.d(TAG,pkg);
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
        //PAGEDOWN is not working in Kindle, so USE VOL-
        //Weread USE PAGEDOWN
        //RootCmd.execRootCmdSilent("input keyevent 93");
        //Kindle USE Vol-
        RootCmd.execRootCmdSilent("input keyevent 25");
    }

    public void PageUp() {
        Log.d(TAG,"Simulate PAGE UP");
        //RootCmd.execRootCmdSilent("input keyevent 92");
        RootCmd.execRootCmdSilent("input keyevent 24");
    }
    public void RefreshEinkDisplay() {
        Log.d(TAG,"RUN setprop sys.eink.reconfigure 1");
        //RootCmd.execRootCmdSilent("setprop sys.eink.reconfigure 1");
    }

    @Override
    public void onInterrupt() {
    }


    public  String GetCurrentRunningPackage(){

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
       return componentInfo.getPackageName();

    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

}