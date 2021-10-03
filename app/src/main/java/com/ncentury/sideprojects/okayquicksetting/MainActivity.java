package com.ncentury.sideprojects.okayquicksetting;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //private RadioButton rbtnVideoMode;
    //private RadioButton rbtnReadMode;
    //private RadioButton rbtnDualMode;
    //private RadioGroup rGroupMode;
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

    public static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPUtil.init(this.getApplicationContext());
        /*
        setContentView(R.layout.activity_main);
        rGroupMode=(RadioGroup)findViewById(R.id.rGroupMode);
        rbtnVideoMode=(RadioButton)findViewById(R.id.rbtnVideoMode);
        rbtnReadMode=(RadioButton)findViewById(R.id.rbtnReadMode);
        rbtnDualMode=(RadioButton)findViewById(R.id.rbtnDualMode);

        TextView versionName = findViewById(R.id.appName);
        versionName.setText(getApplicationName(this)+" v" + BuildConfig.VERSION_NAME);
         */

        upgradeRootPermission(getPackageCodePath());

        Intent paramBundle = new Intent(getApplicationContext(), OkayQuickSettingForegroundService.class);
        paramBundle.setAction("ACTION_START_OKAY_QSFG_SERVICE");
        startService(paramBundle);

        if(!accessibilityServiceEnabled()) {
            Toast.makeText(getApplicationContext(),"辅助功能未打开,按键映射功能不能使用，请先在设置中打开辅助功能选项",Toast.LENGTH_SHORT).show();
        }
        Intent paramBundle2 = new Intent(getApplicationContext(), ButtonRemapService.class);
        paramBundle.setAction("ACTION_START_BTNMAP_SERVICE");
        startService(paramBundle2);


        Intent paramBundle3 = new Intent(getApplicationContext(), OkayQuickSettingForegroundService.class);
        //MODE mode = MODE.valueOf(SPUtil.get("CURRENT_MODE", "DUAL"));
        //MODE mode=MODE.DUAL;
        //String action=mode==MODE.READ?"ACTION_READ_MODE":mode==MODE.VIDEO?"ACTION_VEDIO_MODE":"ACTION_DUAL_MODE";
        //Hard set to VIDEO mode when start up
        String action="ACTION_VIDEO_MODE";
        paramBundle3.setAction(action);
        startService(paramBundle3);
        /*
        if(mode==MODE.VIDEO){
            rbtnVideoMode.setChecked(true);
        }else if(mode==MODE.READ){
            rbtnReadMode.setChecked(true);
        }else if(mode==MODE.DUAL) {
            rbtnDualMode.setChecked(true);
        }


        rGroupMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                MODE mode=MODE.DUAL;
                switch (checkedId){
                    case R.id.rbtnVideoMode:
                        mode=MODE.VIDEO;
                        break;
                    case R.id.rbtnReadMode:
                        mode=MODE.READ;
                        break;
                    case R.id.rbtnDualMode:
                        mode=MODE.DUAL;
                        break;
                }
                Intent paramBundle = new Intent(getApplicationContext(), OkayQuickSettingForegroundService.class);
                String action=mode==MODE.READ?"ACTION_READ_MODE":mode==MODE.VIDEO?"ACTION_VEDIO_MODE":"ACTION_DUAL_MODE";
                paramBundle.setAction(action);
                startService(paramBundle);
            }
        });
         */
        //close main windows
        this.finish();
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
