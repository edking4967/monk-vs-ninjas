package com.thousandonestories.game.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.ViewThread;
import com.thousandonestories.game.bitmap.BitmapCache;

public class NewPanel extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {

	public static GameManager gm;
	private ViewThread mThread;
	private GestureDetectorCompat gDetector;

	public NewPanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		gm = new GameManager(this); 
		BitmapCache.setContext(context);
		gDetector = new GestureDetectorCompat(getContext(), this);
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
		
//		if( gm.getGameState() instanceof GameState_Menu ) 
//			return gm.handleInput( event.getX(), event.getY(), event.getAction() );
//		else if (gm.getGameState() instanceof GameState_Play )
//			return gDetector.onTouchEvent(event);
//		else return false;
		return gm.handleInput(event.getX(), event.getY(), event.getAction() );
	}


	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean onFling(MotionEvent eventOn, MotionEvent eventOff, float velocityX,
			float velocityY) {
		return gm.handleFling( velocityX, velocityY);
		
	}


	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
