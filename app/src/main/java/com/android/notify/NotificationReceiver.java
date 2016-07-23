package com.android.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.qrcodeclient.Card.CardMainActivity;

/**
 * Created by liujq on 16/7/22
 */
public class NotificationReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断app进程是否存活
        if(SystemUtils.isAppAlive(context, "com.android.qrcodeclient")){
            Log.i("NotificationReceiver", "the app process is alive");
            Intent mainIntent = new Intent(context, CardMainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            Intent[] intents = {mainIntent};
            context.startActivities(intents);


        }else {
            Log.i("NotificationReceiver", "the app process is dead");
            Intent launchIntent = context.getPackageManager(). getLaunchIntentForPackage("com.android.qrcodeclient");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
          /*  Bundle args = new Bundle();
            args.putString("name", "");
            args.putString("price", "");
            args.putString("detail", "这是app进程不存在，先启动应用再启动Activity的");
            launchIntent.putExtra(Constants.EXTRA_BUNDLE, args);*/
            context.startActivity(launchIntent);
        }
    }
}
