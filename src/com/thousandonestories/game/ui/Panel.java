package com.thousandonestories.game.ui;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.ViewThread;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Panel extends SurfaceView implements SurfaceHolder.Callback{

	private UIOverlay ui;
	private GameManager gm;
	
	private float panelWidth;
	private float panelHeight;
	
	private ViewThread mThread;
	
	
	public Panel(Context context) {
		super(context);
		
		getHolder().addCallback(this);
		mThread = new ViewThread(this);

		
		ui = new UIOverlay();
		gm = new GameManager(); 
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		panelWidth = width;
		panelHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	public void doDraw(Canvas c)
	{
		
		ui.doDraw(c);
	}

}
