package com.android.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.qrcodeclient.R;

/**
 * Created by liujq on 16/7/22
 */
public class ShowNotificationReceiver extends BroadcastReceiver{
    private static final String TAG = "RepeatReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "ShowNotificationReceiver onReceive");
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder .setContentIntent(pendingIntent) .setSmallIcon(R.mipmap.mylist2)//设置状态栏里面的图标（小图标） 　　　　　　　　　　　　　　　　　　　　
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.mylist2))//下拉下拉列表里面的图标（大图标） 　　　　　　　
                        // .setTicker("this is bitch!") //设置状态栏的显示的信息
                .setWhen(System.currentTimeMillis())//设置时间发生时间
                .setAutoCancel(true)//设置可以清除
                .setContentTitle("消息公告")//设置下拉列表里的标题
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentText("本小区今晚会通水，请大家做好准备");//设置上下文内容


        Log.i("repeat", "showNotification");
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
