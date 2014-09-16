package com.thousandonestories.game.ui;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;

public class Hud {
	
	private CopyOnWriteArrayList<UIBlock> uiBlockList;
	
	public Hud()
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
	
	public void addElement(ClickableSprite cs)
	{
		uiBlockList.add(new UIBlock(cs));
	}
	
	public void addElement(UIBlock b)
	{
		uiBlockList.add(b);
	}
	
}
