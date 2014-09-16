package com.thousandonestories.game.ui;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;

import com.thousandonestories.game.gameobjects.Block;

public class RectangleOverlay {
	private CopyOnWriteArrayList<Block> overlayList;
	
	public RectangleOverlay()
	{
		overlayList = new CopyOnWriteArrayList<Block>();
	}
	
	public void add(Block ... b)
	{
		for(int i = 0; i<b.length; i++)
			overlayList.add(b[i]);
	}
	
	public void doDraw(Canvas c)
	{
		for(Block b: overlayList)
		{
			b.doDraw(c);
		}
	}

	public void remove(Block b) {
		overlayList.remove(b);
	}

}
