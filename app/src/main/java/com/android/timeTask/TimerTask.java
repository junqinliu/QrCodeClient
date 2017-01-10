package com.android.timeTask;


public class TimerTask {

	public long timeInterval; //响铃的间隔时间
	public long reckonTime;   //用于处理何时响铃，初始化时应该设置为0
	public TimerListener timerListener; //监听器，用于回调执行任务
	public String Tag; //用来标识本次任务，相同的标签的任务将会被替换

	public TimerTask(long timeInterval, TimerListener timerListener,String Tag) {
		this.timeInterval = timeInterval;
		this.timerListener = timerListener;
		this.Tag = Tag;
		this.reckonTime = 0;

	}


}
