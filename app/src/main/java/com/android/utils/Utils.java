package com.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;

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
}
