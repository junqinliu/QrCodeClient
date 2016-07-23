package com.android.notify;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.List;
/**
 * Created by liujq 16/07/22
 */

public class ServiceUtil {

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(50);

        if(null == serviceInfos || serviceInfos.size() < 1) {
            return false;
        }

        for(int i = 0; i < serviceInfos.size(); i++) {
            if(serviceInfos.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }

    public static void invokeTimerPOIService(Context context){

        PendingIntent alarmSender = null;
        Intent startIntent = new Intent(context, PushService.class);

        try {
            alarmSender = PendingIntent.getService(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {

        }
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),1000,alarmSender);
       // am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10*1000, alarmSender);


       /*========================================是否要判断SDK版本  =====================*/
       /* if (VERSION.SDK_INT < 19) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, *//* c.getTimeInMillis() *//*
                    System.currentTimeMillis(), intervalTime, operation);
        } else {

            setExact(am, AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), intervalTime, operation);
        }*/
    }

    public static void cancleAlarmManager(Context context){

        Intent intent = new Intent(context,PushService.class);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }
}
