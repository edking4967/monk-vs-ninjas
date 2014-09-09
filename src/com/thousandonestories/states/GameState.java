package com.thousandonestories.states;

import android.graphics.Canvas;

import com.thousandonestories.game.GameManager;

public abstract class GameState {
	
	protected GameManager gm;
	
	public GameState( GameManager gm )
	{
		this.gm = gm;
	}
	
	public abstract boolean handleInput(float x, float y, int action);
	public abstract void update(long dt);
	public abstract void doDraw(Canvas c);

}
