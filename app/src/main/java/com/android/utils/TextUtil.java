package com.android.utils;

import android.widget.TextView;

/**
 * Created by jsx on 2016/4/11.
 */
public class TextUtil {

    /**
     * 判断是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        try {
            String str = null;
            if (obj instanceof String) {
                str = (String) obj;
            } else if (obj instanceof Double) {
                str = (Double) obj + "";
            } else if (obj instanceof Integer) {
                str = (Integer) obj + "";
            } else if (obj instanceof TextView) {
                str = ((TextView) obj).getText().toString();
            }
            if (str == null || "".equals(str) || "null".equals(str) || "NULL".equals(str)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 判断是否为int 类型
     *
     * @param str
     * @return
     */
    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否为double 类型
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 字符串转int
     *
     * @param str
     * @return
     */
    public static int checkInt(String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }

    /**
     * 字符串转double
     *
     * @param str
     * @return
     */
    public static double checkDouble(String str) throws NumberFormatException {
        return Double.parseDouble(str);
    }

    /**
     * 格式化double,取小数点后两位
     * @param str
     * @return
     */
    public static String formatDouble(String str) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        return df.format(str);
    }

    /**
     * 格式化double,取小数点后两位
     * @param str
     * @return
     */
    public static String formatDouble(double str) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        return formatDouble(Double.toString(str));
    }

}
