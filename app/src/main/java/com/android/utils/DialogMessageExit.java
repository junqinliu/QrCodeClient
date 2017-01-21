package com.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.android.application.ExitApplication;
import com.android.qrcodeclient.LoginActivity;
import com.android.timeTask.AlarmManagerTask;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by liujunqin on 2017/01/21.
 */
public class DialogMessageExit {


    private static Context context;
    private static DialogMessageExit dialogMessageUtil;
    public static Dialog noticeDialog;

    public DialogMessageExit(Context context){

        this.context = context;

    }

    public static DialogMessageExit getInstance(Context context){

        if(dialogMessageUtil == null){
            dialogMessageUtil = new DialogMessageExit(context);
        }

        return dialogMessageUtil;
    }


    public   void showDialog(){

        if(!((Activity) context).isFinishing()){


            if(noticeDialog == null){

            noticeDialog =   new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                    .setMessage("该账号已经在别的设备上登录了,将退出应用")
                    .setPositiveButton("退出", dialogListener).create();noticeDialog.setCancelable(false);
            noticeDialog.show();
        }

        }





    }

    // 退出提示框按钮监听
    static DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {


            //跳转到登录接口 并且把本地文件的数据清除掉
            SharedPreferenceUtil.getInstance(context).deleteData();
            //停止定时任务
            AlarmManagerTask.getInstance(context).cancelAlarmManager();
            //  noticeDialog = null;
            //停止极光推送
            JPushInterface.stopPush(context);
            noticeDialog = null;
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);

            ExitApplication.getInstance().exitActivity();
            System.exit(0);

        }
    };

}
