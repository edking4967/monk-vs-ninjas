package com.thousandonestories.game.gameobjects;

import com.thousandonestories.game.SpriteResources;

import android.view.MotionEvent;

public class FlyingSprite extends NewSprite{

	public FlyingSprite(SpriteResources spriteRes, float x, float y, int scalefactor) {
		super(spriteRes, x, y, scalefactor);
	}
	
	public boolean handleEvent(MotionEvent event)
	{
		this.setLeftBound(event.getX());
		this.setTopBound(event.getY());
		return true;
	}

}
