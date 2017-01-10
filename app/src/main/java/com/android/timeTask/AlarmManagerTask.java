package com.android.timeTask;

import java.util.ArrayList;
import java.util.List;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Build.VERSION;

/**
 * 工具类
 * @author liujunqin
 *
 */
public class AlarmManagerTask {

	public static final String TIME_REPEATING = "TimeRepeating";
	public static AlarmManagerTask alarmManagerTask;
	public static int REPEATING = 5 * 1000;
	public List<TimerTask> list;
	private Context context;
	private PendingIntent sender;
	public static AlarmManager am;


	public static AlarmManagerTask getInstance(Context context){

		if(alarmManagerTask == null){

			alarmManagerTask = new AlarmManagerTask();
			alarmManagerTask.list = new ArrayList<TimerTask>();
			alarmManagerTask.context = context;
			alarmManagerTask.startAlarmManager();
		}

		return alarmManagerTask;
	}


	public void startAlarmManager() {

		if(sender == null){

			Intent intent = new Intent(context, TimeReceiver.class);
			intent.setAction(AlarmManagerTask.TIME_REPEATING);
			sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		}

		if (am == null)
			am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		// 开始时间
		long firstime = SystemClock.elapsedRealtime();
		if(VERSION.SDK_INT > 19){

			am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, sender);

		}else{

			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime(), AlarmManagerTask.REPEATING,
					sender);
		}

	}

	/**
	 * 取消闹钟定时器
	 */
	public void cancelAlarmManager() {
		if (sender != null) {
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			am.cancel(sender);
		}

		list = null;
		sender = null;
		alarmManagerTask = null;
		System.gc();
	}



	/**
	 * 增加一个新的任务，相同的任务将会被替换和重置响铃时间
	 *
	 * @param task
	 * @return
	 */
	public boolean addTimerTask(TimerTask task) {

		if(list != null && task != null){

			int length = list.size();
			TimerTask temp = null;
			for(int i = 0; i < length; i++){

				temp = list.get(i);
				if (temp.Tag.equals(task.Tag)) {
					temp.timeInterval = task.timeInterval;
					temp.timerListener = task.timerListener;
					return true;
				}
			}
			return list.add(task);
		}

		return false;
	}

	public void startNow(TimerTask task) {
		if (list != null && task != null) {
			int length = list.size();
			TimerTask temp = null;
			for (int i = 0; i < length; i++) {
				temp = list.get(i);
				if (temp.Tag.equals(task.Tag)) {
					temp.reckonTime = task.timeInterval;
				}

			}
		}
	}

	private boolean removeTimerTask(TimerTask task) {
		if (list != null && task != null) {
			return list.remove(task);
		}
		return false;
	}

	public boolean removeTimerTask(String tag) {
		TimerTask task = getTimerTaskByTag(tag);
		return removeTimerTask(task);
	}

	private TimerTask getTimerTaskByTag(String tag) {
		for (TimerTask task : list) {
			if (task.Tag.equals(tag))
				return task;
		}
		return null;
	}

	public boolean hasTask(String tag) {
		for (TimerTask task : list) {
			if (task.Tag.equals(tag))
				return true;
		}
		return false;
	}

}
