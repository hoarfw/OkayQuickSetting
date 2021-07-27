package com.ncentury.sideprojects.okayquicksetting;

import android.os.Build;

public  class DeviceUtils {
    public static boolean isOkayS4() {
        return "Galileo".equals(Build.MODEL);
    }

    public static boolean isOkayS4Plus() {
        String model=Build.MODEL;
        return "Tesla X510".equals(Build.MODEL);
    }
}
