package com.android.qrcodeclient.Life;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.constant.Constants;

/**
 * Created by liujunqin on 2017/1/12.
 */
public class MyReceiver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        if("cn.jpush.android.intent.NOTIFICATION_OPENED".equals(intent.getAction())){


            Intent intent1 = new Intent(context,MessageActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);



        }

        if(!"cn.jpush.android.intent.REGISTRATION".equals(intent.getAction())){

            Constants.isShowRedPoint = true;
        }


    }
}
