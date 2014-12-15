package com.thousandonestories.game.ui;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;

public class UIOverlay {
	private CopyOnWriteArrayList<UIBlock> uiBlockList;
	
	public UIOverlay()
	{
		uiBlockList = new CopyOnWriteArrayList<UIBlock>();	
	}
	
	public void doDraw(Canvas c)
	{
		for( UIBlock uiB: uiBlockList)
		{
			uiB.doDraw(c);
		}
	}
	
	public void update(long elapsedTime)
	{
		for( UIBlock uiB: uiBlockList)
		{
			uiB.update(elapsedTime);
		}
	}
}
