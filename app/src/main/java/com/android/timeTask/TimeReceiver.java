package com.android.timeTask;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class TimeReceiver extends BroadcastReceiver {
	
	public static String TAG = TimeReceiver.class.getSimpleName();

	@SuppressLint("NewApi")
	public void isRepeating(Context context) {
		if (android.os.Build.VERSION.SDK_INT > 19) {
			Intent intent = new Intent(context, TimeReceiver.class);
			intent.setAction(AlarmManagerTask.TIME_REPEATING);
			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					intent, 0);

			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + AlarmManagerTask.REPEATING,
					sender);
		}
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		
		List<TimerTask> list = AlarmManagerTask.getInstance(context).list;
		
		if(list != null && intent.getAction().equals(AlarmManagerTask.TIME_REPEATING)){
			
			for (int i = 0; i < list.size(); i++) {
				final TimerTask timerTask = list.get(i);
				if (timerTask.timeInterval <= timerTask.reckonTime && timerTask.timerListener != null) {
					timerTask.reckonTime = 0;
//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//							timerTask.timerListener.timeArrival(context);
//						}
//					}).start();

                    //如果定时任务执行的是调接口的时候 不用new Thread
					timerTask.timerListener.timeArrival(context);

				} else {
					timerTask.reckonTime = timerTask.reckonTime + AlarmManagerTask.REPEATING;
				}
			}
			
		}
		
		isRepeating(context);

	}

}
