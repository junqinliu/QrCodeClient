package com.android.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.qrcodeclient.R;
import com.android.utils.TextUtil;

public class UpdateManger {



	// 应用程序Context

	private Context mContext;

	// 提示消息

	private String updateMsg = "有最新的软件包，请下载！";

	// 下载安装包的网络路径

	//private String apkUrl = "http://115.28.6.127:8090/app/1462785029964BirdStore20150929V131.apk";
	private String apkUrl = "";

	private Dialog noticeDialog;// 提示有软件更新的对话框

	private Dialog downloadDialog;// 下载对话框

	private static final String savePath = Environment

			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

			+ "/updateDemo/";// 保存apk的文件夹

	private static final String saveFileName = savePath

			+ "UpdateDemoRelease.apk";

	// 进度条与通知UI刷新的handler和msg常量

	private ProgressBar mProgress;
	private TextView num;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private static final int DOWN_FAIL = 3;

	private int progress;// 当前进度

	private Thread downLoadThread; // 下载线程

	private boolean interceptFlag = false;// 用户取消下载

	// 通知处理刷新界面的handler

	private Handler mHandler = new Handler() {

		@SuppressLint("HandlerLeak")

		@Override

		public void handleMessage(Message msg) {

			switch (msg.what) {

				case DOWN_UPDATE:

					mProgress.setProgress(progress);
					num.setText(progress+"%");

					break;

				case DOWN_OVER:

					downloadDialog.cancel();

					installApk();

					break;

				case DOWN_FAIL:

					Toast.makeText(mContext, "下载出错", Toast.LENGTH_SHORT).show();

					break;

			}

			super.handleMessage(msg);

		}

	};


	public UpdateManger(Context context) {

		this.mContext = context;

	}


	// 显示更新程序对话框，供主程序调用

	public  void checkUpdateInfo(String apkUrlTemp) {
		apkUrl = apkUrlTemp;
		if(!TextUtil.isEmpty(apkUrl)){

			showNoticeDialog();
		}

	}


	private void showNoticeDialog() {

		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(

				mContext, AlertDialog.THEME_HOLO_LIGHT);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息

		builder.setTitle("软件版本更新");

		builder.setMessage(updateMsg);

		builder.setPositiveButton("下载", new OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();// 当取消对话框后进行操作一定的代码？取消对话框

				showDownloadDialog();

			}

		});

		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}

		});

		noticeDialog = builder.create();

		noticeDialog.setCancelable(false);

		noticeDialog.show();

	}


	protected void showDownloadDialog() {

		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(

				mContext);

		builder.setTitle("正在下载");

		final LayoutInflater inflater = LayoutInflater.from(mContext);

		View v = inflater.inflate(R.layout.progress, null);

		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		num = (TextView) v.findViewById(R.id.tv_client_progress);

		builder.setView(v);// 设置对话框的内容为一个View

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

				interceptFlag = true;

			}

		});

		downloadDialog = builder.create();

		downloadDialog.show();

		downloadApk();

	}


	private void downloadApk() {

		downLoadThread = new Thread(mdownApkRunnable);

		downLoadThread.start();

	}


	protected void installApk() {

		File dir = StorageUtils.getCacheDirectory(mContext);

		String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1,

				apkUrl.length());

		File ApkFile = new File(dir, apkName);

		if (!ApkFile.exists()) {

			return;

		}

		Log.e("File.toString()", "" + ApkFile.toString());

		Intent i = new Intent(Intent.ACTION_VIEW);

		//如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装

		String[] command = { "chmod", "777", ApkFile.toString() };

		ProcessBuilder builder = new ProcessBuilder(command);

		try {

			builder.start();

		} catch (IOException e) {

			e.printStackTrace();

		}

		i.setDataAndType(Uri.fromFile(ApkFile),

				"application/vnd.android.package-archive");// File.toString()会返回路径信息

		mContext.startActivity(i);

	}


	private Runnable mdownApkRunnable = new Runnable() {


		@Override

		public void run() {

			URL url;

			try {

				url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url

						.openConnection();

				conn.connect();

				int length = conn.getContentLength();

				InputStream ins = conn.getInputStream();

				// File file = new File(savePath);

				// if (!file.exists()) {

				// boolean b = file.mkdirs();

				// Log.e("exists", saveFileName+","+b);

				// }

				// String apkFile = saveFileName;

				// Log.e("exists2", saveFileName);

				// File ApkFile = new File(apkFile);


				File dir = StorageUtils.getCacheDirectory(mContext);

				String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1,

						apkUrl.length());

				File ApkFile = new File(dir, apkName);


				FileOutputStream outStream = new FileOutputStream(ApkFile);

				int count = 0;

				byte buf[] = new byte[1024];

				do {

					int numread = ins.read(buf);

					count += numread;

					progress = (int) (((float) count / length) * 100);

					// 下载进度

					mHandler.sendEmptyMessage(DOWN_UPDATE);

					if (numread <= 0) {

						// 下载完成通知安装

						mHandler.sendEmptyMessage(DOWN_OVER);

						break;

					}

					outStream.write(buf, 0, numread);

				} while (!interceptFlag);// 点击取消停止下载

				outStream.close();

				ins.close();

			} catch (Exception e) {

				Log.e("Exception", "" + e.getMessage().toString());

				mHandler.sendEmptyMessage(DOWN_FAIL);

				e.printStackTrace();

			}

		}

	};


}
