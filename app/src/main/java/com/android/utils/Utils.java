package com.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;

import com.android.qrcodeclient.Card.CardMainActivity;
import com.android.qrcodeclient.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * 工具类
 */
@SuppressLint("SimpleDateFormat")
public class Utils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dps
     * @return
     */
    public static int dpToPx(Context context,int dps) {
        return Math.round(context.getResources().getDisplayMetrics().density * dps);
    }

    /**
     * 获取屏幕分辨率的宽度
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        return mDisplay.getWidth();
    }

    /**
     * 获取屏幕分辨率的高度
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        return mDisplay.getHeight();
    }


    /**
     * 生成二维码
     *
     * @param context
     * @param url
     * @return
     */
    public static Bitmap createQRImage(Context context, String url, int width, int height) {
        Bitmap bitmap = null;
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return bitmap;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < width; y++) {
                for (int x = 0; x < height; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /*public void notifyKJ(Context context,String str) {
        //获得通知管理器，通知是一项系统服务
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //初始化通知对象 p1:通知的图标 p2:通知的状态栏显示的提示 p3:通知显示的时间
        Notification notification = new Notification(R.mipmap.mylist2, "提醒", System.currentTimeMillis());
        //点击通知后的Intent，此例子点击后还是在当前界面
        Intent notificationIntent = new Intent(context, CardMainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        //设置通知信息
      //  notification.setLatestEventInfo(context, "提醒", str, contentIntent);
        notification.

        notification.flags|=Notification.FLAG_AUTO_CANCEL; //当查看后，自动消失
        notification.defaults |= Notification.DEFAULT_SOUND;//默认声音提示
        //通知
        manager.notify(1, notification);
    }*/


      public static void showNotification(Context context,String str) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent contentIndent = PendingIntent.getActivity(context, 0, new Intent(context,CardMainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder .setContentIntent(contentIndent) .setSmallIcon(R.mipmap.mylist2)//设置状态栏里面的图标（小图标） 　　　　　　　　　　　　　　　　　　　　
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.mylist2))//下拉下拉列表里面的图标（大图标） 　　　　　　　
                // .setTicker("this is bitch!") //设置状态栏的显示的信息
                .setWhen(System.currentTimeMillis())//设置时间发生时间
                .setAutoCancel(true)//设置可以清除
                .setContentTitle("消息公告")//设置下拉列表里的标题
                .setContentText("本小区今晚会通水，请大家做好准备");//设置上下文内容
        Notification notification = builder.getNotification();
          notification.flags|=Notification.FLAG_AUTO_CANCEL; //当查看后，自动消失
          notification.defaults |= Notification.DEFAULT_SOUND;//默认声音提示
        //加i是为了显示多条Notification
        notificationManager.notify(1,notification);
    }


    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
