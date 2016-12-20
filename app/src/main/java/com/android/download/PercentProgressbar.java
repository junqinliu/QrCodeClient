package com.android.download;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.widget.ProgressBar;

public class PercentProgressbar extends ProgressBar{
	
	
	String text;
	Paint mTextPaint;

	public PercentProgressbar(Context context) {
		super(context);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		setText(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect = new Rect();
		this.mTextPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mTextPaint);
	}

	private void initText() {
		this.mTextPaint = new TextPaint();
		this.mTextPaint.setColor(Color.WHITE);
		//�����������������������
	}

	private void setText(int progress){
		int i = (progress * 100)/this.getMax();
		this.text = String.valueOf(i) + "%";
	}
	

}
