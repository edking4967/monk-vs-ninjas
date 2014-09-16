package com.thousandonestories.game.ui;

import java.util.concurrent.CopyOnWriteArrayList;


import android.graphics.Canvas;

public class UIBlock {

	protected CopyOnWriteArrayList<ClickableSprite> uiItems;
	
	public UIBlock()
	{
		uiItems = new CopyOnWriteArrayList<ClickableSprite>();
	}
	
	public UIBlock( ClickableSprite... cslist )
	{
		this();
		for(ClickableSprite cs: cslist)
		{
			uiItems.add(cs);
		}
	}
	

	public void doDraw(Canvas canvas) {
		for(ClickableSprite uiItem: uiItems)
		{
			uiItem.doDraw(canvas);
		}
	}

	public void update(long elapsedTime) {
		for(ClickableSprite uiItem: uiItems)
		{
			uiItem.update(elapsedTime);
		}
	}

}
