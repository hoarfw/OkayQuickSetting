package com.ncentury.sideprojects.okayquicksetting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity {
    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); // 切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upgradeRootPermission(getPackageCodePath());

        Intent paramBundle = new Intent(getApplicationContext(), OkayQuickSettingForegroundService.class);
        paramBundle.setAction("ACTION_START_OKAY_QSFG_SERVICE");
        startService(paramBundle);
        finish();
    }
}