package com.ncentury.sideprojects.okayquicksetting;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ScreenUtils {
    public static MODE CURRENT_MODE=MODE.UNKNOWN;
    public static MODE LAST_MODE=MODE.UNKNOWN;
    public static void exeCommand(String paramString) throws IOException, TimeoutException {
         RootCmd.execRootCmdSilent(paramString + "\n");
    }

    public static void OpenMainDisplay() throws IOException, TimeoutException {
        exeCommand("setprop sys.close.mainTp 0");
        exeCommand("setprop sys.close.mainPen 0");
        exeCommand("setprop ro.core_ctl_min_cpu 2");
        exeCommand("setprop ro.core_ctl_max_cpu 4");
        //exeCommand("wm size 1200x1920");
        //exeCommand("wm density 300");
        exeCommand("chmod 777 /sys/class/leds/lcd-backlight/max_brightness");
        exeCommand("echo 255 > /sys/class/leds/lcd-backlight/max_brightness");
        exeCommand("chmod 444 /sys/class/leds/lcd-backlight/max_brightness");
        exeCommand("chmod 777 /sys/class/leds/lcd-backlight/brightness");
        exeCommand("echo 255 > /sys/class/leds/lcd-backlight/brightness");
        exeCommand("chmod 444 /sys/class/leds/lcd-backlight/brightness");
    }

    public static void CloseMainDisplay() throws IOException, TimeoutException {

        exeCommand("setprop sys.close.mainTp 1");
        exeCommand("setprop sys.close.mainPen 1");
        exeCommand("chmod 777 /sys/class/leds/lcd-backlight/max_brightness");
        exeCommand("echo 0 > /sys/class/leds/lcd-backlight/max_brightness");
        exeCommand("chmod 444 /sys/class/leds/lcd-backlight/max_brightness");
        exeCommand("chmod 777 /sys/class/leds/lcd-backlight/brightness");
        exeCommand("echo 0 > /sys/class/leds/lcd-backlight/brightness");
        exeCommand("chmod 444 /sys/class/leds/lcd-backlight/brightness");
    }

    public static void OpenEinkDisplay() throws IOException, TimeoutException {
        //打开副屏：打开副屏显示+触摸，显示合适分辨率，设置方向，设置合适参数
        //exeCommand("setprop sys.open.eink.power 1");
        exeCommand("setprop sys.close.subTp 0");
        exeCommand("setprop sys.close.SubPen 0");
        //exeCommand("wm size 1404x1872");
        //exeCommand("wm density 300");
        exeCommand("setprop sys.eink.Appmode 13");
        exeCommand("setprop persist.eink.GLD_Refresh_Count 10");
        exeCommand("setprop ro.core_ctl_min_cpu 1");
        exeCommand("setprop ro.core_ctl_max_cpu 1");
        exeCommand("setprop persist.demo.hdmirotation portrait");
        exeCommand("setprop sys.eink.reconfigure 1");
    }

    public static void CloseEinkDisplay() throws IOException, TimeoutException {
        //打开副屏：打开副屏显示+触摸，显示合适分辨率，设置方向，设置合适参数
        //exeCommand("setprop sys.open.eink.power 0");
        exeCommand("setprop sys.close.subTp 1");
        exeCommand("setprop sys.close.SubPen 1");
    }

    public static void ReconfigEinkDisplay() throws IOException, TimeoutException {
        //重新加载配置
        exeCommand("qemu.hw.mainkeys 1");
        //exeCommand("setprop sys.eink.reconfigure 1");
        exeCommand("input keyevent 26 ");
        exeCommand("input keyevent 82 ");
        exeCommand("input keyevent 3 ");
    }

    public static void EnableMirrorMode() throws IOException, TimeoutException {
        exeCommand("setprop sys.eink.forcemirror true");
    }

    public static void DisableMirrorMode() throws IOException, TimeoutException {
        exeCommand("setprop sys.eink.forcemirror false");
    }

    public static void AdjustResolution() throws IOException, TimeoutException {
        if( CURRENT_MODE==MODE.READ && LAST_MODE!=MODE.READ){
            exeCommand("wm size 1404x1872");
            exeCommand("wm density 300");
        }else if (CURRENT_MODE!=MODE.READ && LAST_MODE==MODE.READ){
            exeCommand("wm size 1200x1920");
            exeCommand("wm density 300");
        }
        exeCommand("ro.rotation.external true");
        exeCommand("qemu.hw.mainkeys 1");
    }
}
