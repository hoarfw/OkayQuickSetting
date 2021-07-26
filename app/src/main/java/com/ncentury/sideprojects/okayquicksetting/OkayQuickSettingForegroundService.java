package com.ncentury.sideprojects.okayquicksetting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;



import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OkayQuickSettingForegroundService  extends Service {
    public static final String ACTION_READ_MODE = "ACTION_READ_MODE";
    public static final String ACTION_VEDIO_MODE = "ACTION_VEDIO_MODE";
    public static final String ACTION_DUAL_MODE = "ACTION_DUAL_MODE";
    public static final String ACTION_TURN_OFF_LCD = "ACTION_TURN_OFF_LCD";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_OKAY_QSFG_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_OKAY_QSFG_SERVICE";
    private static final String TAG_FOREGROUND_SERVICE = "OKAY_QSFG_SERVICE";

    //private IntentFilter intentFilter;
    private Context mContext;
    //private RingerModeChangeReceiver ringerModeChangeReceiver;
    private ScreenReceiver screenReceiver;



    public void onCreate()
    {
        super.onCreate();
        this.mContext = this;
        //this.intentFilter = new IntentFilter();
        //this.intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        //this.ringerModeChangeReceiver = new RingerModeChangeReceiver();
        //registerReceiver(this.ringerModeChangeReceiver, this.intentFilter);
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        screenReceiver = new ScreenReceiver();
        registerReceiver(screenReceiver, filter);
    }

    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(this.screenReceiver);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startForegroundService() throws IOException {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");


        NotificationManager manager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Object localObject = PendingIntent.getActivity(this, 0, new Intent(), 0);
        NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(this,"default");
        NotificationCompat.BigTextStyle localBigTextStyle = new NotificationCompat.BigTextStyle();
        localBigTextStyle.setBigContentTitle("OKAY屏幕一键切换v1.0.3 - Ken.J.(Q519637737)");
        localBigTextStyle.bigText("");
        localBuilder.setStyle(localBigTextStyle);
        localBuilder.setWhen(System.currentTimeMillis());
        localBuilder.setSmallIcon(R.drawable.tablet);
        localBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.tablet));
        localBuilder.setPriority(2);
        localBuilder.setFullScreenIntent((PendingIntent) localObject, true);
        localObject = new Intent(this, OkayQuickSettingForegroundService.class);
        ((Intent) localObject).setAction(ACTION_VEDIO_MODE);
        localBuilder.addAction(new NotificationCompat.Action( R.drawable.video, "视频模式", PendingIntent.getService(this, 0, (Intent) localObject, 0)));

        localObject = new Intent(this, OkayQuickSettingForegroundService.class);
        ((Intent) localObject).setAction(ACTION_READ_MODE);
        localBuilder.addAction(new NotificationCompat.Action(R.drawable.book, "阅读模式", PendingIntent.getService(this, 0, (Intent) localObject, 0)));

        localObject = new Intent(this, OkayQuickSettingForegroundService.class);
        ((Intent) localObject).setAction(ACTION_DUAL_MODE);
        localBuilder.addAction(new NotificationCompat.Action(R.drawable.dual, "双屏模式", PendingIntent.getService(this, 0, (Intent) localObject, 0)));

        localObject = new Intent(this, OkayQuickSettingForegroundService.class);
        ((Intent) localObject).setAction(ACTION_TURN_OFF_LCD);
        localBuilder.addAction(new NotificationCompat.Action(R.drawable.tablet, "关闭主屏", PendingIntent.getService(this, 0, (Intent) localObject, 0)));

        Notification notification=localBuilder.build();
        //startForeground(1, localBuilder.build());
        manager.notify(1,notification);




    }

    private void stopForegroundService() throws IOException {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");

        stopForeground(true);
        stopSelf();

    }


    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        try {
            String action = paramIntent.getAction();

            if(action==ACTION_TURN_OFF_LCD){
                ScreenUtils.CloseMainDisplay();

            }else {
                ScreenUtils.LAST_MODE = ScreenUtils.CURRENT_MODE;
                if (action == ACTION_VEDIO_MODE && ScreenUtils.CURRENT_MODE != MODE.VIDEO) {
                    ScreenUtils.CURRENT_MODE = MODE.VIDEO;
                    ScreenUtils.DisableMirrorMode();
                    ScreenUtils.CloseMainDisplay();
                    ScreenUtils.AdjustResolution();
                    ScreenUtils.OpenMainDisplay();
                    Toast.makeText(getApplicationContext(), "视频模式", Toast.LENGTH_SHORT).show();
                } else if (action == ACTION_READ_MODE && ScreenUtils.CURRENT_MODE != MODE.READ) {
                    ScreenUtils.CURRENT_MODE = MODE.READ;
                    ScreenUtils.CloseMainDisplay();
                    ScreenUtils.OpenEinkDisplay();
                    ScreenUtils.AdjustResolution();
                    ScreenUtils.EnableMirrorMode();
                    ScreenUtils.CloseMainDisplay();
                    Toast.makeText(getApplicationContext(), "阅读模式", Toast.LENGTH_SHORT).show();
                } else if (action == ACTION_DUAL_MODE && ScreenUtils.CURRENT_MODE != MODE.DUAL) {
                    ScreenUtils.CURRENT_MODE = MODE.DUAL;

                    ScreenUtils.CloseMainDisplay();
                    ScreenUtils.OpenEinkDisplay();
                    ScreenUtils.AdjustResolution();
                    ScreenUtils.OpenMainDisplay();
                    ScreenUtils.EnableMirrorMode();
                    Toast.makeText(getApplicationContext(), "双屏模式", Toast.LENGTH_SHORT).show();
                } else if (action == ACTION_START_FOREGROUND_SERVICE) {
                    startForegroundService();
                    Toast.makeText(getApplicationContext(), "OKAY一键切换屏幕开始运行", Toast.LENGTH_SHORT).show();
                } else if (action == ACTION_STOP_FOREGROUND_SERVICE) {
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "OKAY一键切换屏幕停止运行", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }

}
