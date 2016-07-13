package com.android.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jsx on 2016/4/11.
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm
     */
    public static String getCurrentDate() {
        return getCurrentDate(DateType.N_YMdHm);
    }

    /**
     * 获取当前时间
     *
     * @param dateType 时间的类型
     * @return
     */
    public static String getCurrentDate(@NonNull DateType dateType) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType.getType());
        return dateFormat.format(now);
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return
     */
    public static String dateToStr(@NonNull Date date) {
        return dateToStr(date, DateType.N_YMdHm);
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @param dateType
     * @return
     */
    public static String dateToStr(@NonNull Date date, @NonNull DateType dateType) {
        SimpleDateFormat format = new SimpleDateFormat(dateType.getType());
        return format.format(date);
    }

    /**
     * 日期字符串转Data类型
     *
     * @param str
     * @return
     */
    public static Date strToDate(@NonNull String str) {
        return strToDate(DateType.N_YMdHm.getType());
    }

    /**
     * 日期字符串转Data类型
     *
     * @param str
     * @param dateType
     * @return Date or null
     */
    public static Date strToDate(@NonNull String str, @NonNull DateType dateType) {
        SimpleDateFormat format = new SimpleDateFormat(dateType.getType());
        Date date = new Date();
        try {
            date = format.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
