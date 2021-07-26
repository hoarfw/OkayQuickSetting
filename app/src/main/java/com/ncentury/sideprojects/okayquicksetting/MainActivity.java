package com.ncentury.sideprojects.okayquicksetting;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
        //setContentView(R.layout.activity_main);
        upgradeRootPermission(getPackageCodePath());

        Intent paramBundle = new Intent(getApplicationContext(), OkayQuickSettingForegroundService.class);
        paramBundle.setAction("ACTION_START_OKAY_QSFG_SERVICE");
        startService(paramBundle);
        //if(!accessibilityServiceEnabled()) {
        //    Toast.makeText(getApplicationContext(),"辅助功能未打开，请先在设置中打开辅助功能选项",Toast.LENGTH_SHORT).show();
        //}
        //Intent paramBundle2 = new Intent(getApplicationContext(), ButtonRemapService.class);
        //paramBundle.setAction("ACTION_START_BTNMAP_SERVICE");
        //startService(paramBundle2);
        finish();
    }

    private boolean accessibilityServiceEnabled() {
        final String service =  "com.ncentury.sideprojects.okayquicksetting/" + ButtonRemapService.class.getCanonicalName();

        TextUtils.SimpleStringSplitter stringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (settingValue != null) {
            stringColonSplitter.setString(settingValue);
            while (stringColonSplitter.hasNext()) {
                String accessibilityService = stringColonSplitter.next();

                if (accessibilityService.equalsIgnoreCase(service)) {
                    return true;
                }
            }
        }

        return false;
    }
}
