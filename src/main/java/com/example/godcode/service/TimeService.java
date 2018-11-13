package com.example.godcode.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.LogUtil;

/**
 * Created by Administrator on 2018/9/7.
 */

public class TimeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

      AlarmManager  manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime =  SystemClock.elapsedRealtime()  +(Constant.expireInSeconds-60)*1000;
        Intent intent1 = new Intent("ELITOR_CLOCK");
        LogUtil.log("---------timeService-----"+ SystemClock.elapsedRealtime());
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent1, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
