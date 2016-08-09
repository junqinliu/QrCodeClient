package com.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by liujunqin on 2016/8/8.
 */
public class DialogMessageUtil {


    private Context context;
    private static DialogMessageUtil dialogMessageUtil;

    public DialogMessageUtil(Context context){

        this.context = context;

    }

    public static DialogMessageUtil getInstance(Context context){

        if(dialogMessageUtil == null){
            dialogMessageUtil = new DialogMessageUtil(context);
        }

        return dialogMessageUtil;
    }


    public static  void showDialog(Context context,String str){
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                .setMessage(str)
               /* .setNegativeButton("取消", null)*/
                .setPositiveButton("确定", dialogListener).create().show();

    }

    // 退出提示框按钮监听
    static android.content.DialogInterface.OnClickListener dialogListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {



        }
    };

}
