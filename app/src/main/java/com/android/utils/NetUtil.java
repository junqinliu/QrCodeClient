package com.android.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings;

public class NetUtil {

	public static NetUtil instance = null;

	private NetUtil() {
	}

	public static NetUtil getInstance() {
		if (instance == null)
			instance = new NetUtil();
		return instance;
	}

	/**
	 * 判断是否3G网络
	 */
	public static boolean isMobileNet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null ? State.DISCONNECTED : cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	
		return mobile == State.CONNECTED;
	}

	/**
	 * 判断是否是WIFI网络
	 */
	public static boolean isWifiNet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取WIFI状�?
		State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		return wifi == State.CONNECTED;
	}

	/**
	 * 判断网络连接状态
	 */
	public static boolean checkNetInfo(Context context) {
		return NetUtil.isMobileNet(context)
				|| NetUtil.isWifiNet(context);
	}

	/**
	 * 是否设置网络状态
	 */
	public static void openConfigNetInfo(final Context context) {
		if (checkNetInfo(context) == false) {
			Dialog dialog = new AlertDialog.Builder(context).setTitle(
					"当前网络不可�?").setMessage("是否设置你的网络").setPositiveButton("确定",
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							context.startActivity(new Intent(
									Settings.ACTION_WIRELESS_SETTINGS));
						}

					}).setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).create();
			dialog.show();
		}
	}
}