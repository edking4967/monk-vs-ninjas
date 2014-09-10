package com.thousandonestories.game.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.ViewThread;

public class NewPanel extends SurfaceView implements SurfaceHolder.Callback{

	public static GameManager gm;
	private ViewThread mThread;


	public NewPanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		gm = new GameManager(this); 
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		gm.setDimensions(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!mThread.isAlive()) {
			mThread = new ViewThread(this);
			mThread.setRunning(true);
			mThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        if (mThread.isAlive()) {
                mThread.setRunning(false);
        }
	}

	public void doDraw(Canvas c)
	{
		c.drawColor(Color.RED);
		gm.doDraw(c);
	}

	public void update(long elapsedTime) {
		gm.update(elapsedTime);
	}

	public ViewThread getThread() {
		return mThread;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		GestureDetectorCompat g;
		VelocityTracker v;
		return gm.handleInput( event.getX(), event.getY(), event.getAction() );
	}

}
